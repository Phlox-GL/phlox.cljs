
(ns phlox.app.container
  (:require [phlox.core :refer [defcomp rect circle text container graphics create-list]]
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
            :align "center"},
    :alpha 1})
  (create-list
   :container
   {}
   (->> (range 20)
        (map
         (fn [idx]
           [idx
            (text
             {:text (str idx),
              :style {:font-family "Helvetica Neue",
                      :font-weight 300,
                      :font-size 14,
                      :fill (hslx 200 10 (+ 40 (* 4 idx)))},
              :position {:x (+ 200 (* idx 20)), :y (+ 140 (* idx 10))},
              :rotation (* 0.1 (+ idx (:x store)))})]))))
  (graphics
   {:ops [[:line-style {:width 4, :color (hslx 200 80 80), :alpha 1}]
          [:begin-fill {:color (hslx 0 80 20)}]
          [:move-to {:x 100, :y 200}]
          [:line-to {:x 400, :y 400}]
          [:line-to {:x 500, :y 300}]
          [:close-path]],
    :rotation 0.1,
    :pivot {:x 0, :y 100},
    :alpha 0.5})))
