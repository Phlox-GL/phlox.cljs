
(ns phlox.render
  (:require ["pixi.js" :as PIXI]
            [phlox.util
             :refer
             [use-number component? element? remove-nil-values index-items map-to-object]]
            [phlox.util.lcs :refer [find-minimal-ops lcs-state-0]]
            [phlox.render.draw
             :refer
             [call-graphics-ops
              set-position
              set-pivot
              set-rotation
              set-alpha
              add-events
              update-events
              set-line-style
              draw-circle
              draw-rect]]))

(declare render-children)

(declare render-element)

(declare render-rect)

(declare render-container)

(declare render-graphics)

(declare render-circle)

(declare render-text)

(declare update-element)

(declare update-children)

(defn render-text [element dispatch!]
  (let [style (:style (:props element))
        text-style (new (.-TextStyle PIXI) (map-to-object style))
        target (new (.-Text PIXI) (:text (:props element)) text-style)
        props (:props element)]
    (set-position target (:position props))
    (set-pivot target (:pivot props))
    (set-rotation target (:rotation props))
    (set-alpha target (:alpha props))
    (render-children target (:children element) dispatch!)
    target))

(defn render-rect [element dispatch!]
  (let [target (new (.-Graphics PIXI))
        props (:props element)
        line-style (:line-style props)
        options (:options props)
        events (:on props)]
    (if (some? (:fill props)) (.beginFill target (:fill props)))
    (set-line-style target line-style)
    (draw-rect target options)
    (if (some? (:fill props)) (.endFill target))
    (set-position target (:position props))
    (set-pivot target (:pivot props))
    (set-rotation target (:rotation props))
    (set-alpha target (:alpha props))
    (add-events target events dispatch!)
    (render-children target (:children element) dispatch!)
    target))

(defn render-graphics [element dispatch!]
  (let [target (new (.-Graphics PIXI))
        props (:props element)
        ops (:ops props)
        events (:on props)]
    (call-graphics-ops target ops)
    (set-rotation target (:rotation props))
    (set-pivot target (:pivot props))
    (set-position target (:position props))
    (set-alpha target (:alpha props))
    (add-events target events dispatch!)
    (render-children target (:children element) dispatch!)
    target))

(defn render-element [element dispatch!]
  (case (:phlox-node element)
    :element
      (case (:name element)
        nil nil
        :container (render-container element dispatch!)
        :graphics (render-graphics element dispatch!)
        :circle (render-circle element dispatch!)
        :rect (render-rect element dispatch!)
        :text (render-text element dispatch!)
        (do (println "unknown tag:" (:tag element)) {}))
    :component (render-element (:tree element) dispatch!)
    (do (js/console.error "Unknown element:" element))))

(defn render-container [element dispatch!]
  (let [target (new (.-Container PIXI)), props (:props element)]
    (render-children target (:children element) dispatch!)
    (set-position target (:position props))
    (set-rotation target (:rotation props))
    (set-pivot target (:pivot props))
    (set-alpha target (:alpha props))
    target))

(defn render-circle [element dispatch!]
  (let [target (new (.-Graphics PIXI))
        props (:props element)
        line-style (:line-style props)
        options (:options props)
        events (:on props)]
    (when (some? (:fill props)) (.beginFill target (:fill props)))
    (set-line-style target line-style)
    (draw-circle target options)
    (when (some? (:fill props)) (.endFill target))
    (add-events target events dispatch!)
    (set-position target (:position props))
    (set-alpha target (:alpha props))
    (render-children target (:children element) dispatch!)
    target))

(defn render-children [target children dispatch!]
  (doseq [child-pair children]
    (if (some? child-pair)
      (.addChild target (render-element (last child-pair) dispatch!))
      (js/console.log "nil child:" child-pair))))

(defn update-circle [element old-element target dispatch!]
  (let [props (:props element)
        props' (:props old-element)
        options (:options props)
        options' (:options props')
        line-style (:line-style props)
        line-style' (:line-style props')]
    (when (or (not= options options')
              (not= line-style line-style')
              (not= (:fill props) (:fill props')))
      (.clear target)
      (when (some? (:fill props)) (.beginFill target (:fill props)))
      (set-line-style target line-style)
      (draw-circle target options)
      (when (some? (:fill props)) (.endFill target))
      (when (not= (:alpha props) (:alpha props')) (set-alpha target (:alpha props)))
      (when (not= (:position props) (:position props'))
        (set-position target (:position props))))
    (update-events target (-> element :props :on) (-> old-element :props :on) dispatch!)))

(defn update-container [element old-element target]
  (let [props (:props element), props' (:props old-element)]
    (when (not= (:position props) (:position props')) (set-position target (:position props)))
    (when (not= (:pivot props) (:pivot props')) (set-pivot target (:pivot props)))
    (when (not= (:rotation props) (:rotation props'))
      (set-rotation target (:rotation props)))
    (when (not= (:alpha props) (:alpha props')) (set-alpha target (:alpha props)))))

(defn update-graphics [element old-element target dispatch!]
  (let [props (:props element)
        props' (:props old-element)
        ops (:ops props)
        ops' (:ops props')]
    (when (not= ops ops') (.clear target) (call-graphics-ops target ops))
    (when (not= (:position props) (:position props'))
      (set-position target (:position props)))
    (when (not= (:rotation props) (:rotation props'))
      (set-rotation target (:rotation props)))
    (when (not= (:pivot props) (:pivot props')) (set-pivot target (:pivot props)))
    (when (not= (:alpha props) (:alpha props')) (set-alpha target (:alpha props)))
    (update-events target (-> element :props :on) (-> old-element :props :on) dispatch!)))

(defn update-rect [element old-element target dispatch!]
  (let [props (:props element)
        props' (:props old-element)
        options (:options props)
        options' (:options props')
        line-style (:line-style props)
        line-style' (:line-style props')]
    (when (or (not= options options')
              (not= line-style line-style')
              (not= (:fill props) (:fill props')))
      (.clear target)
      (if (some? (:fill props)) (.beginFill target (:fill props)))
      (set-line-style target line-style)
      (draw-rect target options)
      (if (some? (:fill props)) (.endFill target)))
    (when (not= (:position props) (:position props'))
      (set-position target (:position props)))
    (when (not= (:rotation props) (:rotation props'))
      (set-rotation target (:rotation props)))
    (when (not= (:pivot props) (:pivot props')) (set-pivot target (:pivot props)))
    (when (not= (:alpha props) (:alpha props')) (set-pivot target (:alpha props)))
    (update-events target (-> element :props :on) (-> old-element :props :on) dispatch!)))

(defn update-text [element old-element target]
  (let [props (:props element)
        props' (:props old-element)
        text-style (:style props)
        text-style' (:style props')]
    (when (not= (:text props) (:text props')) (set! (.-text target) (:text props)))
    (when (not= text-style text-style')
      (let [new-style (new (.-TextStyle PIXI) (map-to-object text-style))]
        (set! (.-style target) new-style)))
    (when (not= (:position props) (:position props'))
      (set-position target (:position props)))
    (when (not= (:rotation props) (:rotation props'))
      (set-rotation target (:rotation props)))
    (when (not= (:pivot props) (:pivot props')) (set-pivot target (:pivot props)))
    (when (not= (:alpha props) (:alpha props')) (set-alpha target (:alpha props)))))

(defn update-element [element old-element parent-element idx dispatch! options]
  (cond
    (or (nil? element) (nil? element)) (js/console.error "Not supposed to be empty")
    (and (component? element)
         (component? old-element)
         (= (:name element) (:name old-element)))
      (if (and (= (:args element) (:args old-element)) (not (:swap? options)))
        (comment
         do
         (js/console.log "Same, no changes" (:name element))
         (js/console.log (:args element) (:args old-element)))
        (recur (:tree element) (:tree old-element) parent-element idx dispatch! options))
    (and (component? element) (element? old-element))
      (recur (:tree element) old-element parent-element idx dispatch! options)
    (and (element? element) (component? old-element))
      (recur element (:tree old-element) parent-element idx dispatch! options)
    (and (element? element) (element? old-element) (= (:name element) (:name old-element)))
      (do
       (let [target (.getChildAt parent-element idx)]
         (case (:name element)
           :container (update-container element old-element target)
           :circle (update-circle element old-element target dispatch!)
           :rect (update-rect element old-element target dispatch!)
           :text (update-text element old-element target)
           :graphics (update-graphics element old-element target dispatch!)
           (do (println "not implement yet for updating:" (:name element)))))
       (update-children
        (:children element)
        (:children old-element)
        (.getChildAt parent-element idx)
        dispatch!
        options))
    (not= (:name element) (:name old-element))
      (do
       (.removeChildAt parent-element idx)
       (.addChildAt parent-element (render-element element dispatch!) idx))
    :else (js/console.warn "Unknown case:" element old-element)))

(defn update-children [children-dict old-children-dict parent-container dispatch! options]
  (assert
   (and (every? some? (map last children-dict)) (every? some? (map last old-children-dict)))
   "children should not contain nil element")
  (let [list-ops (:acc
                  (find-minimal-ops
                   lcs-state-0
                   (map first old-children-dict)
                   (map first children-dict)))]
    (comment js/console.log "ops" list-ops old-children-dict children-dict)
    (loop [idx 0, ops list-ops, xs children-dict, ys old-children-dict]
      (when-not (empty? ops)
        (let [op (first ops)]
          (case (first op)
            :remains
              (do
               (assert (= (last op) (first (first xs)) (first (first ys))) "check key")
               (update-element
                (last (first xs))
                (last (first ys))
                parent-container
                idx
                dispatch!
                options)
               (recur (inc idx) (rest ops) (rest xs) (rest ys)))
            :add
              (do
               (assert (= (last op) (first (first xs))) "check key")
               (.addChildAt
                parent-container
                (render-element (last (first xs)) dispatch!)
                idx)
               (recur (inc idx) (rest ops) (rest xs) ys))
            :remove
              (do
               (assert (= (last op) (first (first ys))) "check key")
               (.removeChildAt parent-container idx)
               (recur idx (rest ops) xs (rest ys)))
            (do (println "Unknown op:" op))))))))
