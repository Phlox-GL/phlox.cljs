
(ns phlox.app.container
  (:require [phlox.core
             :refer
             [defcomp g hslx rect circle text container graphics create-list]]
            [phlox.app.comp.drafts :refer [comp-drafts]]
            [phlox.app.comp.keyboard :refer [comp-keyboard]]))

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
         (g :bezier-to {:p1 [400 500], :p2 [300 200], :to-p [600 300]})
         (comment g :line-to [400 400])]}))

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
        (mapcat (fn [x] (->> (range 30) (map (fn [y] [x y])))))
        (map
         (fn [[x y]]
           [(str x "+" y)
            (rect
             {:position [(* x 14) (* y 14)],
              :size [10 10],
              :fill (hslx 200 80 80),
              :on {:mouseover (fn [e d!] (println "hover:" x y))}})]))))))

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
  (comp-tab-entry :gradients "Gradients" [10 250] (= :gradients tab))
  (comp-tab-entry :keyboard "Keyboard" [10 300] (= :keyboard tab))
  (comp-tab-entry :slider "Slider" [10 350] (= :slider tab))
  (comp-tab-entry :buttons "Buttons" [10 400] (= :buttons tab))))

(defcomp
 comp-container
 (store)
 (comment println "Store" store (:tab store))
 (container
  {}
  (comp-tabs (:tab store))
  (case (:tab store)
    :drafts (comp-drafts (:x store))
    :grids (comp-grids)
    :curves (comp-curves)
    :gradients (comp-gradients)
    :keyboard (comp-keyboard (:keyboard-on? store) (:counted store))
    (text
     {:text "Unknown",
      :style {:fill (hslx 0 100 80), :font-size 12, :font-family "Helvetica"}}))))
