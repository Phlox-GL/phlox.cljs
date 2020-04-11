
(ns phlox.app.container
  (:require [phlox.core
             :refer
             [defcomp g hslx rect circle text container graphics create-list >>]]
            [phlox.app.comp.drafts :refer [comp-drafts]]
            [phlox.app.comp.keyboard :refer [comp-keyboard]]
            [phlox.comp.button :refer [comp-button]]
            [phlox.comp.drag-point :refer [comp-drag-point]]
            [phlox.comp.switch :refer [comp-switch]]
            [phlox.app.comp.slider-demo :refer [comp-slider-demo]]
            [phlox.input :refer [request-text!]]
            [phlox.comp.messages :refer [comp-messages]]
            ["shortid" :as shortid]))

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
   {:text "Blue", :position [100 60], :color (hslx 0 80 70), :fill (hslx 200 80 40)})
  (comp-button
   {:text "Short hand click",
    :position [100 120],
    :on-click (fn [e d!] (println "clicked"))})))

(defcomp
 comp-curves
 ()
 (graphics
  {:ops [(g :line-style {:width 2, :color (hslx 200 80 80), :alpha 1})
         (g :move-to [0 0])
         (g :line-to [100 200])
         (g :arc-to {:p1 [200 200], :p2 [240 180], :radius 90})
         (g :arc {:center [260 120], :radius 40, :angle [70 60], :anticlockwise? false})
         (g :quadratic-to {:p1 [400 100], :to-p [500 400]})
         (g :bezier-to {:p1 [400 500], :p2 [300 200], :to-p [600 300]})
         (g :begin-fill {:color (hslx 200 80 80), :alpha 1})
         (g :arc {:center [600 300], :radius 20, :angle [0 300], :anticlockwise? false})
         (g :end-fill nil)
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
 comp-messages-demo
 (states)
 (let [cursor (:cursor states), state (or (:data states) {:messages [], :bottom? false})]
   (container
    {}
    (comp-button
     {:text "Add message",
      :position [400 200],
      :on-click (fn [e d!]
        (d!
         cursor
         (update
          state
          :messages
          (fn [xs]
            (conj
             xs
             {:id (.generate shortid), :text (str "Messages of. " (.generate shortid))})))))})
    (comp-switch
     {:value (:bottom? state),
      :title "At bottom",
      :position [400 280],
      :on-change (fn [e d!] (d! cursor (update state :bottom? not)))})
    (comp-messages
     {:messages (:messages state),
      :bottom? (:bottom? state),
      :on-click (fn [message d!]
        (d!
         cursor
         (update
          state
          :messages
          (fn [xs] (->> xs (remove (fn [x] (= (:id x) (:id message)))) (vec))))))}))))

(defcomp
 comp-points-demo
 (states)
 (let [cursor (:cursor states)
       state (or (:data states) {:p1 [0 0], :p2 [0 20], :p3 [0 40], :p4 [0 60], :p5 [0 80]})]
   (container
    {:position [300 200]}
    (comp-drag-point
     (>> states :p1)
     {:position (:p1 state),
      :on-change (fn [position d!] (d! cursor (assoc state :p1 position)))})
    (comp-drag-point
     (>> states :p2)
     {:position (:p2 state),
      :unit 2,
      :on-change (fn [position d!] (d! cursor (assoc state :p2 position)))})
    (comp-drag-point
     (>> states :p3)
     {:position (:p3 state),
      :unit 0.4,
      :radius 6,
      :fill (hslx 0 90 60),
      :color (hslx 0 0 50),
      :on-change (fn [position d!] (d! cursor (assoc state :p3 position)))})
    (comp-drag-point
     (>> states :p4)
     {:position (:p4 state),
      :title "base",
      :on-change (fn [position d!] (d! cursor (assoc state :p4 position)))})
    (comp-drag-point
     (>> states :p5)
     {:position (:p5 state),
      :hide-text? true,
      :on-change (fn [position d!] (d! cursor (assoc state :p5 position)))}))))

(defcomp
 comp-switch-demo
 (states)
 (let [cursor (:cursor states), state (or (:data states) {:value false})]
   (container
    {:position [300 300]}
    (comp-switch
     {:value (:value state),
      :position [0 0],
      :on-change (fn [value d!] (d! cursor (assoc state :value value)))})
    (comp-switch
     {:value (:value state),
      :position [100 20],
      :title "Custom title",
      :on-change (fn [value d!] (d! cursor (assoc state :value value)))}))))

(defcomp
 comp-tab-entry
 (tab-value tab-title idx selected?)
 (container
  {:position [10 (+ 50 (* idx 40))]}
  (rect
   {:position [0 0],
    :size [160 32],
    :fill (if selected? (hslx 180 50 50) (hslx 180 50 30)),
    :on {:mousedown (fn [event dispatch!] (dispatch! :tab tab-value))}})
  (text
   {:text tab-title,
    :style {:fill (hslx 200 90 100), :font-size 20, :font-family "Josefin Sans"},
    :position [10 3]})))

(defcomp
 comp-text-input
 ()
 (container
  {}
  (rect
   {:position [200 10],
    :size [40 20],
    :fill (hslx 0 0 20),
    :on {:click (fn [e d!] (request-text! e {} (fn [result] (println "got:" result))))}})))

(def tabs
  [[:drafts "Drafts"]
   [:grids "Grids"]
   [:curves "Curves"]
   [:gradients "Gradients"]
   [:keyboard "Keyboard"]
   [:slider "Slider"]
   [:buttons "Buttons"]
   [:points "Points"]
   [:switch "Switch"]
   [:input "Input"]
   [:messages "Messages"]])

(defcomp
 comp-container
 (store)
 (comment println "Store" store (:tab store))
 (let [cursor [], states (:states store)]
   (container
    {}
    (create-list
     :container
     {}
     (->> tabs
          (map-indexed
           (fn [idx info]
             (let [[tab title] info]
               [idx (comp-tab-entry tab title idx (= tab (:tab store)))])))))
    (case (:tab store)
      :drafts (comp-drafts (:x store))
      :grids (comp-grids)
      :curves (comp-curves)
      :gradients (comp-gradients)
      :keyboard (comp-keyboard (:keyboard-on? store) (:counted store))
      :buttons (comp-buttons)
      :slider (comp-slider-demo (>> states :slider))
      :points (comp-points-demo (>> states :points))
      :switch (comp-switch-demo (>> states :switch))
      :input (comp-text-input)
      :messages (comp-messages-demo (>> states :messages))
      (text
       {:text "Unknown",
        :style {:fill (hslx 0 100 80), :font-size 12, :font-family "Helvetica"}})))))
