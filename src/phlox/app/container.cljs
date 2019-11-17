
(ns phlox.app.container
  (:require [phlox.core :refer [defcomp rect circle text container]]
            [phlox.util :refer [hslx]]))

(defcomp
 comp-container
 (store)
 (container
  {:options {:x 0, :y 0}, :rotation 0}
  (circle
   {:options {:x 200, :y 100, :radius 40},
    :line-style {:width 2, :color (hslx 0 80 50), :alpha 1},
    :fill (hslx 160 80 70),
    :on {:mousedown (fn [event dispatch!] (dispatch! :add-x "a"))}})
  (rect
   {:options {:x 0, :y 0, :width 50, :height 50},
    :line-style {:width 2, :color (hslx 200 80 80), :alpha 1},
    :fill (hslx 200 80 80),
    :on {:mousedown (fn [e dispatch!] (dispatch! :add-x "b"))},
    :rotation (+ 1 (* 0.1 (:x store))),
    :pivot {:x 0, :y 0},
    :position {:x 100, :y 100}}
   (text
    {:text (str
            "Text demo:"
            (+ 1 (* 0.1 (:x store)))
            "\n"
            "pivot"
            (pr-str {:x 100, :y 100})),
     :style {:font-family "Menlo", :font-size 12, :fill (hslx 200 80 90), :align "center"}}))
  (text
   {:text (str "Text demo:" (:x store)),
    :style {:font-family "Menlo",
            :font-size 12,
            :fill (hslx 200 80 (+ 80 (* 20 (js/Math.random)))),
            :align "center"}})))
