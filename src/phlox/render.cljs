
(ns phlox.render
  (:require ["pixi.js" :as PIXI]
            [phlox.util :refer [use-number component? element?]]
            [phlox.util.lcs :refer [find-minimal-ops lcs-state-0]]))

(declare render-container)

(declare render-element)

(defn render-circle [element]
  (let [circle (new (.-Graphics PIXI))
        props (:props element)
        line-style (:line-style props)
        options (:options props)
        events (:on props)]
    (if (some? (:fill props)) (.beginFill circle (:fill props)))
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
    (if (some? (:fill props)) (.endFill circle))
    (when (some? events)
      (set! (.-interactive circle) true)
      (set! (.-buttonMode circle) true)
      (doseq [[k listener] events]
        (println "binding" (name k) listener)
        (.on circle (name k) listener)))
    circle))

(defn render-rect [element]
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
    (when (some? events)
      (set! (.-interactive rect) true)
      (set! (.-buttonMode rect) true)
      (doseq [[k listener] events] (.on rect (name k) listener)))
    rect))

(defn render-element [element]
  (case (:phlox-node element)
    :element
      (case (:name element)
        nil (do (js/console.log "nil element" element) nil)
        :container (render-container element)
        :graphics (let [g (new (.-Graphics PIXI))] g)
        :circle (render-circle element)
        :rect (render-rect element)
        (do (println "unknown tag:" (:tag element)) {}))
    :component (render-element (:tree element))
    (do (js/console.log "Unknown element:" element))))

(defn render-container [element]
  (let [container (new (.-Container PIXI)), options (:options (:props element))]
    (doseq [child (:children element)]
      (if (some? child)
        (.addChild container (render-element child))
        (js/console.log "nil child:" child)))
    (when (some? options)
      (set! (.-x container) (:x options))
      (set! (.-y container) (:y options)))
    container))

(defn update-children [children-dict old-children-dict parent-container]
  (let [list-ops (:acc
                  (find-minimal-ops
                   lcs-state-0
                   (map first children-dict)
                   (map first old-children-dict)))]
    (println "changes to children" list-ops)))

(defn update-element [element old-element]
  (js/console.log "refresh" element old-element)
  (cond
    (or (nil? element) (nil? element)) (js/console.warn "Not supposed to be empty")
    (and (component? element)
         (component? old-element)
         (= (:name element) (:name old-element)))
      (if (= (:args element) (:args old-element))
        (do "Same changes")
        (recur (:tree element) (:tree old-element)))
    (and (element? element)
         (element? old-element)
         (= (:name element) (:name old-element))
         (do
          (println "handle element change" element old-element)
          (println "hanle children")))
      :else))
