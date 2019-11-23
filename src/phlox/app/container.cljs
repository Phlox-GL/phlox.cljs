
(ns phlox.app.container
  (:require [phlox.core :refer [defcomp rect circle text container graphics create-list]]
            [phlox.util :refer [hslx]]))

(defcomp
 comp-drafts
 (x)
 (container
  {:position {:x 100, :y 100}, :rotation 0}
  (circle
   {:options {:x 200, :y 100, :radius 40},
    :line-style {:width 2, :color (hslx 0 80 50), :alpha 1},
    :fill (hslx 160 80 70),
    :on {:mousedown (fn [event dispatch!] (dispatch! :add-x nil))}})
  (rect
   {:options {:x 0, :y 0, :width 50, :height 50},
    :line-style {:width 2, :color (hslx 200 80 80), :alpha 1},
    :fill (hslx 200 80 80),
    :on {:mousedown (fn [e dispatch!] (dispatch! :add-x nil))},
    :rotation (+ 1 (* 0.1 x)),
    :pivot {:x 0, :y 0},
    :position {:x 100, :y 100}}
   (text
    {:text (str "Text demo:" (+ 1 (* 0.1 x)) "\n" "pivot" (pr-str {:x 100, :y 100})),
     :style {:font-family "Menlo", :font-size 12, :fill (hslx 200 80 90), :align "center"}}))
  (text
   {:text (str "Text demo:" x),
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
              :rotation (* 0.1 (+ idx x))})]))))
  (graphics
   {:ops [[:line-style {:width 4, :color (hslx 200 80 80), :alpha 1}]
          [:begin-fill {:color (hslx 0 80 20)}]
          [:move-to {:x (+ (* 20 x) 100), :y 200}]
          [:line-to {:x (+ (* 20 x) 400), :y 400}]
          [:line-to {:x (- 500 (* 20 x)), :y 300}]
          [:close-path]],
    :rotation 0.1,
    :pivot {:x 0, :y 100},
    :alpha 0.5,
    :on {:pointerdown (fn [e dispatch!] (println "clicked"))}})))

(defcomp
 comp-tabs
 ()
 (container
  {}
  (container
   {:position {:x 10, :y 100}}
   (rect
    {:options {:x 0, :y 0, :width 80, :height 32},
     :fill (hslx 100 90 80),
     :on {:mousedown (fn [event dispatch!] (dispatch! :tab :drafts))}})
   (text
    {:text "Drafts",
     :style {:fill (hslx 200 90 60), :font-size 20, :font-family "Helvetica"}}))
  (container
   {:position {:x 10, :y 200}}
   (rect
    {:options {:x 0, :y 0, :width 160, :height 32},
     :fill (hslx 100 90 80),
     :on {:mousedown (fn [event dispatch!] (dispatch! :tab :repeated))}})
   (text
    {:text "Repeated shapes",
     :style {:fill (hslx 200 90 60), :font-size 20, :font-family "Helvetica"}}))))

(defcomp
 comp-container
 (store)
 (println "Store" store (:tab store))
 (container
  {}
  (comp-tabs)
  (case (:tab store)
    :drafts (comp-drafts (:x store))
    :repeated
      (container
       {}
       (text
        {:text "Repeated",
         :position {:x 200, :y 0},
         :style {:fill (hslx 200 80 80), :font-size 20, :font-family "Helvetica"}}))
    (text
     {:text "Unknown",
      :style {:fill (hslx 0 100 80), :font-size 12, :font-family "Helvetica"}}))))
