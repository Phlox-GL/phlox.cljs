
(ns phlox.render (:require ["pixi.js" :as PIXI] [phlox.util :refer [use-number]]))

(defn render-circle [element]
  (let [circle (new (.-Graphics PIXI))
        props (:props element)
        line-style (:line-style props)
        options (:options props)]
    (if (some? (:fill props)) (.beginFill circle (:fill props)))
    (when (some? line-style)
      (.lineStyle
       circle
       (use-number (:width line-style))
       (use-number (:color line-style))
       (:alpha line-style)))
    (.drawCircle
     circle
     (use-number (:x options))
     (use-number (:y options))
     (use-number (:radius options)))
    circle))

(defn render-element [element]
  (case (:tag element)
    nil (do (js/console.log "nil element" element) nil)
    :container
      (let [container (new (.-Container PIXI))]
        (doseq [child (:children element)]
          (if (some? child)
            (.addChild container (render-element child))
            (js/console.log "nil child:" child)))
        container)
    :graphics (let [g (new (.-Graphics PIXI))] g)
    :circle (render-circle element)
    :rect (let [g (new (.-Graphics PIXI))] g)
    (do (println "unknown tag:" (:tag element)) {})))
