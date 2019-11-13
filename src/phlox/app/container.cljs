
(ns phlox.app.container
  (:require [phlox.core :refer [render-tag]] [phlox.util :refer [hslx]]))

(defn comp-container [store]
  (println "store" store)
  {:name :container,
   :phlox-node :component,
   :args [store],
   :tree (render-tag
          :container
          {:options {:x 100, :y 0}}
          (render-tag
           :circle
           {:options {:x (+ 80 (:x store)), :y 100, :radius 40},
            :line-style {:width 2, :color (hslx 0 80 50), :alpha 1},
            :fill (hslx 160 80 70),
            :on {:mousedown (fn [event dispatch!]
                   (js/console.log "circle click" event)
                   (dispatch! :add-x "a"))}})
          (render-tag
           :rect
           {:options {:x 200, :y 100, :width 50, :height 50},
            :line-style {:width 2, :color (hslx 200 80 80), :alpha 1},
            :fill (hslx 200 80 80),
            :on {:mousedown (fn [] (println "click rect"))}})
          (comment render-tag :container {})
          (comment render-tag :graphics {})
          (comment render-tag :graphics {}))})
