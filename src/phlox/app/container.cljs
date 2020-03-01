
(ns phlox.app.container
  (:require [phlox.core
             :refer
             [defcomp g hslx rect circle text container graphics create-list]]
            [phlox.app.comp.drafts :refer [comp-drafts]]
            [phlox.app.comp.keyboard :refer [comp-keyboard]]
            [phlox.comp.button :refer [comp-button]]
            [phlox.comp.slider :refer [comp-slider]]
            [phlox.comp.drag-point :refer [comp-drag-point]]))

(defcomp
 comp-buttons
 ()
 (container
  {:position [300 100]}
  (comp-button
   {:text "DEMO BUTTON",
    :position [100 0],
    :on {:click (fn [e d!] (js/console.log "clicked" e d!))}})
  (comp-button
   {:text "Blue", :position [100 60], :color (hslx 0 80 70), :fill (hslx 200 80 40)})))

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
 comp-points-demo
 (cursor states)
 (let [state (or (:data states) {:p1 [0 0], :p2 [0 0], :p3 [0 0]})]
   (container
    {:position [300 200]}
    (comp-drag-point
     (conj cursor :p1)
     (:p1 states)
     {:position (:p1 state),
      :on-change (fn [position d!] (d! cursor (assoc state :p1 position)))})
    (comp-drag-point
     (conj cursor :p2)
     (:p2 states)
     {:position (:p2 state),
      :unit 2,
      :on-change (fn [position d!] (d! cursor (assoc state :p2 position)))})
    (comp-drag-point
     (conj cursor :p3)
     (:p3 states)
     {:position (:p3 state),
      :unit 0.4,
      :radius 6,
      :fill (hslx 0 90 60),
      :color (hslx 0 0 50),
      :on-change (fn [position d!] (d! cursor (assoc state :p3 position)))}))))

(defcomp
 comp-slider-demo
 (cursor states)
 (let [state (or (:data states) {:a 40, :b 20, :c 10})]
   (container
    {:position [300 100]}
    (comp-slider
     (conj cursor :a)
     (:a states)
     {:value (:a state),
      :unit 1,
      :position [20 0],
      :on-change (fn [value d!] (d! cursor (assoc state :a value)))})
    (comp-slider
     (conj cursor :b)
     (:b states)
     {:value (:b state),
      :title "Refine",
      :unit 0.1,
      :position [20 60],
      :on-change (fn [value d!] (d! cursor (assoc state :b value)))})
    (comp-slider
     (conj cursor :c)
     (:c states)
     {:value (:c state),
      :unit 10,
      :position [20 120],
      :fill (hslx 50 90 70),
      :color (hslx 200 90 30),
      :on-change (fn [value d!] (d! cursor (assoc state :c value)))}))))

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
  (comp-tab-entry :buttons "Buttons" [10 400] (= :buttons tab))
  (comp-tab-entry :points "Points" [10 450] (= :points tab))))

(defcomp
 comp-container
 (store)
 (comment println "Store" store (:tab store))
 (let [cursor [], states (:states store)]
   (container
    {}
    (comp-tabs (:tab store))
    (case (:tab store)
      :drafts (comp-drafts (:x store))
      :grids (comp-grids)
      :curves (comp-curves)
      :gradients (comp-gradients)
      :keyboard (comp-keyboard (:keyboard-on? store) (:counted store))
      :buttons (comp-buttons)
      :slider (comp-slider-demo (conj cursor :slider) (:slider states))
      :points (comp-points-demo (conj cursor :points) (:points states))
      (text
       {:text "Unknown",
        :style {:fill (hslx 0 100 80), :font-size 12, :font-family "Helvetica"}})))))
