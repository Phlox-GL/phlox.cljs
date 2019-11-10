
(ns phlox.app.container
  (:require [phlox.core :refer [render-tag]] [phlox.util :refer [hslx]]))

(defn comp-container []
  (do
   (comment
    render-tag
    :container
    {}
    (comment render-tag :container {})
    (comment render-tag :graphics {})
    (comment render-tag :graphics {})
    (comment render-tag :rect {}))
   (render-tag
    :circle
    {:options {:x 100, :y 100, :radius 40},
     :line-style {:width 2, :color (hslx 0 80 50), :alpha 1},
     :fill 0x4444ff})))
