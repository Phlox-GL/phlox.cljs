
(ns phlox.render
  (:require ["pixi.js" :as PIXI]
            [phlox.util
             :refer
             [use-number component? element? remove-nil-values index-items map-to-object]]
            [phlox.util.lcs :refer [find-minimal-ops lcs-state-0]]))

(declare render-element)

(declare render-rect)

(declare render-container)

(declare render-circle)

(declare update-element)

(declare update-children)

(defn render-text [element]
  (js/console.log element)
  (let [style (:style (:props element))
        text-style (new (.-TextStyle PIXI) (map-to-object style))
        text (new (.-Text PIXI) (:text (:props element)) text-style)]
    text))

(defn render-rect [element dispatch!]
  (let [rect (new (.-Graphics PIXI))
        props (:props element)
        line-style (:line-style props)
        options (:options props)
        events (:on props)]
    (if (some? (:fill props)) (.beginFill rect (:fill props)))
    (when (some? line-style)
      (.lineStyle
       rect
       (use-number (:width line-style))
       (use-number (:color line-style))
       (:alpha line-style)))
    (if (map? options)
      (.drawRect
       rect
       (use-number (:x options))
       (use-number (:y options))
       (use-number (:width options))
       (use-number (:height options)))
      (js/console.warn "Unknown options" options))
    (if (some? (:fill props)) (.endFill rect))
    (when (some? (:position props))
      (set! (-> rect .-position .-x) (-> props :position :x))
      (set! (-> rect .-position .-y) (-> props :position :y)))
    (when (some? (:pivot props))
      (set! (-> rect .-pivot .-x) (-> props :pivot :x))
      (set! (-> rect .-pivot .-y) (-> props :pivot :y)))
    (when (some? (:rotation props)) (set! (.-rotation rect) (:rotation props)))
    (when (some? events)
      (println (:pivot props))
      (set! (.-interactive rect) true)
      (set! (.-buttonMode rect) true)
      (doseq [[k listener] events]
        (.on rect (name k) (fn [event] (listener event dispatch!)))))
    (doseq [child (:children element)]
      (if (some? child)
        (.addChild rect (render-element child dispatch!))
        (js/console.log "nil child:" child)))
    rect))

(defn render-element [element dispatch!]
  (case (:phlox-node element)
    :element
      (case (:name element)
        nil (do (js/console.log "nil element" element) nil)
        :container (render-container element dispatch!)
        :graphics (let [g (new (.-Graphics PIXI))] g)
        :circle (render-circle element dispatch!)
        :rect (render-rect element dispatch!)
        :text (render-text element)
        (do (println "unknown tag:" (:tag element)) {}))
    :component
      (let [renderer (:render element), tree (apply renderer (:args element))]
        (render-element tree dispatch!))
    (do (js/console.log "Unknown element:" element))))

(defn render-container [element dispatch!]
  (let [container (new (.-Container PIXI)), options (:options (:props element))]
    (doseq [child (:children element)]
      (if (some? child)
        (.addChild container (render-element child dispatch!))
        (js/console.log "nil child:" child)))
    (when (some? options)
      (set! (.-x container) (:x options))
      (set! (.-y container) (:y options)))
    container))

(defn render-circle [element dispatch!]
  (let [circle (new (.-Graphics PIXI))
        props (:props element)
        line-style (:line-style props)
        options (:options props)
        events (:on props)]
    (when (some? (:fill props)) (.beginFill circle (:fill props)))
    (when (some? line-style)
      (.lineStyle
       circle
       (use-number (:width line-style))
       (use-number (:color line-style))
       (:alpha line-style)))
    (if (map? options)
      (.drawCircle
       circle
       (use-number (:x options))
       (use-number (:y options))
       (use-number (:radius options)))
      (js/console.warn "Unknown options" options))
    (when (some? (:fill props)) (.endFill circle))
    (when (some? events)
      (set! (.-interactive circle) true)
      (set! (.-buttonMode circle) true)
      (doseq [[k listener] events]
        (.on circle (name k) (fn [event] (listener event dispatch!)))))
    (doseq [child (:children element)]
      (if (some? child)
        (.addChild circle (render-element child dispatch!))
        (js/console.log "nil child:" child)))
    circle))

(defn update-circle [element old-element circle dispath!]
  (let [props (:props element)
        props' (:props old-element)
        options (:options props)
        options' (:options props')
        line-style (:line-style props)
        line-style' (:line-style props')]
    (when (or (not= options options')
              (not= line-style line-style')
              (not= (:fill props) (:fill props')))
      (.clear circle)
      (when (some? (:fill props)) (.beginFill circle (:fill props)))
      (when (some? line-style)
        (.lineStyle
         circle
         (use-number (:width line-style))
         (use-number (:color line-style))
         (:alpha line-style)))
      (if (map? options)
        (.drawCircle
         circle
         (use-number (:x options))
         (use-number (:y options))
         (use-number (:radius options)))
        (js/console.warn "Unknown options" options))
      (when (some? (:fill props)) (.endFill circle)))))

(defn update-container [element old-element container]
  (let [props (:props element)
        props' (:props old-element)
        options (:options props)
        options' (:options props')]
    (when (not= options options')
      (set! (.-x container) (:x options))
      (set! (.-y container) (:y options)))))

(defn update-rect [element old-element rect]
  (let [props (:props element)
        props' (:props old-element)
        options (:options props)
        options' (:options props')
        line-style (:line-style props)
        line-style' (:line-style props')]
    (when (or (not= options options')
              (not= line-style line-style')
              (not= (:fill props) (:fill props'))
              (not= (:pivot props) (:pivor props'))
              (not= (:rotation props) (:rotation props')))
      (.clear rect)
      (if (some? (:fill props)) (.beginFill rect (:fill props)))
      (when (some? line-style)
        (.lineStyle
         rect
         (use-number (:width line-style))
         (use-number (:color line-style))
         (:alpha line-style)))
      (if (map? options)
        (.drawRect
         rect
         (use-number (:x options))
         (use-number (:y options))
         (use-number (:width options))
         (use-number (:height options)))
        (js/console.warn "Unknown options" options))
      (if (some? (:fill props)) (.endFill rect))
      (when (some? (:position props))
        (set! (-> rect .-position .-x) (-> props :position :x))
        (set! (-> rect .-position .-y) (-> props :position :y)))
      (when (some? (:pivot props))
        (println (:pivot props))
        (set! (-> rect .-pivot .-x) (-> props :pivot :x))
        (set! (-> rect .-pivot .-y) (-> props :pivot :y)))
      (when (some? (:rotation props)) (set! (.-rotation rect) (:rotation props))))))

(defn update-text [element old-element target]
  (let [props (:props element)
        props' (:props old-element)
        text-style (:style props)
        text-style' (:style props')]
    (when (not= (:text props) (:text props')) (set! (.-text target) (:text props)))
    (when (not= text-style text-style')
      (let [new-style (new (.-TextStyle PIXI) (map-to-object text-style))]
        (set! (.-style target) new-style)))))

(defn update-element [element old-element parent-element idx dispath! options]
  (cond
    (or (nil? element) (nil? element)) (js/console.error "Not supposed to be empty")
    (and (component? element)
         (component? old-element)
         (= (:name element) (:name old-element)))
      (if (and (= (:args element) (:args old-element)) (not (:swap? options)))
        (do (println "Same, no changes") (js/console.log (:args element) (:args old-element)))
        (recur
         (apply (:render element) (:args element))
         (apply (:render old-element) (:args old-element))
         parent-element
         idx
         dispath!
         options))
    (and (element? element) (element? old-element) (= (:name element) (:name old-element)))
      (do
       (let [target (.getChildAt parent-element idx)]
         (case (:name element)
           :container (update-container element old-element target)
           :circle (update-circle element old-element target dispath!)
           :rect (update-rect element old-element target)
           :text (update-text element old-element target)
           (do (println "not implement yet for updating:" (:name element)))))
       (update-children
        (remove-nil-values (index-items (:children element)))
        (remove-nil-values (index-items (:children old-element)))
        (.getChildAt parent-element idx)
        dispath!
        options))
    :else (js/console.log "replace element")))

(defn update-children [children-dict old-children-dict parent-container dispath! options]
  (assert
   (and (every? some? (map last children-dict)) (every? some? (map last old-children-dict)))
   "children should not contain nil element")
  (let [list-ops (:acc
                  (find-minimal-ops
                   lcs-state-0
                   (map first old-children-dict)
                   (map first children-dict)))]
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
                dispath!
                options)
               (recur (inc idx) (rest ops) (rest xs) (rest ys)))
            :add
              (do
               (assert (= (:value op) (first (first ys))) "check key")
               (println "add element" (last (first ys)))
               (recur (inc idx) (rest ops) xs (rest ys)))
            :remove
              (do
               (assert (= (:value op) (first (first xs))) "check key")
               (println "remove" idx)
               (recur idx (rest ops) (rest xs) ys))
            (do (println "Unknown op:" op))))))))
