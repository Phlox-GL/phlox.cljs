
(ns phlox.app.container
  (:require [phlox.core
             :refer
             [defcomp g hslx rect circle text container graphics create-list]]))

(defcomp
 comp-curves
 ()
 (graphics
  {:ops [(g :line-style {:width 4, :color (hslx 200 80 80), :alpha 1})
         (g :move-to [0 0])
         (g :line-to [100 200])
         (g :arc-to {:p1 [200 200], :p2 [240 180], :radius 90})
         (g :arc {:center [260 120], :radius 40, :angle [70 60], :anticlockwise? false})
         (g :quadratic-to {:p1 [400 100], :to-p [500 400]})
         (g :bezier-to {:p1 [400 500], :p2 [300 200], :to-p [600 300]})]}))

(defcomp
 comp-drafts
 (x)
 (container
  {:position [200 100], :rotation 0}
  (circle
   {:position [200 100],
    :radius 40,
    :line-style {:width 2, :color (hslx 0 80 50), :alpha 1},
    :fill (hslx 160 80 70),
    :on {:mousedown (fn [event dispatch!] (dispatch! :add-x nil))}})
  (rect
   {:position [100 100],
    :size [50 50],
    :line-style {:width 2, :color (hslx 200 80 80), :alpha 1},
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

(defcomp
 comp-gradients
 ()
 (container
  {}
  (text
   {:text "long long text",
    :position [200 160],
    :style {:fill [(hslx 0 0 100) (hslx 0 0 40)], :fill-gradient-type :v}})
  (text
   {:text "long long text",
    :position [200 200],
    :style {:fill [(hslx 0 0 100) (hslx 0 0 40)], :fill-gradient-type :h}})
  (text {:text "long long text", :position [200 120], :style {:fill (hslx 20 90 60)}})))

(defcomp
 comp-grids
 ()
 (container
  {}
  (create-list
   :container
   {:position [200 20]}
   (->> (range 40)
        (mapcat (fn [x] (->> (range 20) (map (fn [y] [x y])))))
        (map
         (fn [[x y]]
           [(str x "+" y)
            (rect
             {:position [(* x 14) (* y 14)],
              :size [10 10],
              :fill (hslx 200 80 80),
              :on {:mouseover (fn [e d!] (println "hover:" x y))}})]))))
  (rect
   {:position [300 320],
    :size [40 30],
    :fill (hslx 40 80 80),
    :on {:pointerdown (fn [e d!] (println "corsur"))}})))

(defcomp
 comp-tab-entry
 (tab-value tab-title position selected?)
 (container
  {:position position}
  (rect
   {:position [0 0],
    :size [160 32],
    :fill (if selected? (hslx 180 50 50) (hslx 180 50 30)),
    :on {:mousedown (fn [event dispatch!] (dispatch! :tab tab-value))}})
  (text
   {:text tab-title,
    :style {:fill (hslx 200 90 100), :font-size 20, :font-family "Helvetica"},
    :position [10 0]})))

(defcomp
 comp-tabs
 (tab)
 (container
  {}
  (comp-tab-entry :drafts "Drafts" [10 100] (= :drafts tab))
  (comp-tab-entry :grids "Grids" [10 150] (= :grids tab))
  (comp-tab-entry :curves "Curves" [10 200] (= :curves tab))
  (comp-tab-entry :gradients "Gradients" [10 250] (= :gradients tab))))

(defcomp
 comp-container
 (store)
 (println "Store" store (:tab store))
 (container
  {}
  (comp-tabs (:tab store))
  (case (:tab store)
    :drafts (comp-drafts (:x store))
    :grids (comp-grids)
    :curves (comp-curves)
    :gradients (comp-gradients)
    (text
     {:text "Unknown",
      :style {:fill (hslx 0 100 80), :font-size 12, :font-family "Helvetica"}}))))
