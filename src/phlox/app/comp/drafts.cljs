
(ns phlox.app.comp.drafts
  (:require [phlox.core
             :refer
             [defcomp g hslx rect circle text container graphics create-list]]))

(defcomp
 comp-drafts
 (x)
 (container
  {:position [400 100], :rotation 0}
  (circle
   {:position [200 100],
    :radius 40,
    :line-style {:width 4, :color (hslx 0 80 50), :alpha 1},
    :fill (hslx 160 80 70),
    :on {:mousedown (fn [event dispatch!] (dispatch! :add-x nil))}})
  (rect
   {:position [40 40],
    :size [50 50],
    :line-style {:width 4, :color (hslx 0 80 50), :alpha 1},
    :fill (hslx 200 80 80),
    :on {:mousedown (fn [e dispatch!] (dispatch! :add-x nil))},
    :rotation (+ 1 (* 0.1 x)),
    :pivot [0 0]}
   (text
    {:text (str "Text demo:" (+ 1 (* 0.1 x)) "\n" "pivot" (pr-str {:x 100, :y 100})),
     :style {:font-family "Menlo", :font-size 12, :fill (hslx 200 80 90), :align :center}}))
  (text
   {:text (str "Text demo:" x),
    :style {:font-family "Menlo",
            :font-size 12,
            :fill (hslx 200 80 (+ 80 (* 20 (js/Math.random)))),
            :align :center},
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
              :position [(+ 200 (* idx 20)) (+ 140 (* idx 10))],
              :rotation (* 0.1 (+ idx x))})]))))
  (graphics
   {:ops [[:line-style {:width 4, :color (hslx 200 80 80), :alpha 1}]
          [:begin-fill {:color (hslx 0 80 20)}]
          [:move-to [(+ (* 20 x) 100) 200]]
          [:line-to [(+ (* 20 x) 400) 400]]
          [:line-to [(- 500 (* 20 x)) 300]]
          [:close-path]],
    :rotation 0.1,
    :pivot [0 100],
    :alpha 0.5,
    :on {:pointerdown (fn [e dispatch!] (println "clicked"))}})))
