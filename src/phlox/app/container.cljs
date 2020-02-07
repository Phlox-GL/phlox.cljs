
(ns phlox.app.container
  (:require [phlox.core :refer [defcomp hslx rect circle text container graphics create-list]]))

(defcomp
 comp-circle-tree
 ()
 (container
  {:position {:x 200, :y 100}}
  (text
   {:text "Tree", :style {:fill (hslx 200 80 80), :font-size 18, :font-family "Helvetica"}})))

(defcomp
 comp-drafts
 (x)
 (container
  {:position {:x 200, :y 100}, :rotation 0}
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
 comp-grids
 ()
 (container
  {}
  (create-list
   :container
   {:position {:x 200, :y 20}}
   (->> (range 40)
        (mapcat (fn [x] (->> (range 20) (map (fn [y] [x y])))))
        (map
         (fn [[x y]]
           [(str x "+" y)
            (rect
             {:options {:x (* x 14), :y (* y 14), :width 10, :height 10},
              :fill (hslx 200 80 80),
              :on {:mouseover (fn [e d!] (println "d" x y))}})]))))
  (rect
   {:options {:x 30, :y 320, :width 40, :height 30},
    :fill (hslx 40 80 80),
    :on {:pointerdown (fn [e d!] (println "corsur"))}})))

(defcomp
 comp-tab-entry
 (tab-value tab-title position selected?)
 (container
  {:position position}
  (rect
   {:options {:x 0, :y 0, :width 160, :height 32},
    :fill (if selected? (hslx 180 50 50) (hslx 180 50 30)),
    :on {:mousedown (fn [event dispatch!] (dispatch! :tab tab-value))}})
  (text
   {:text tab-title,
    :style {:fill (hslx 200 90 100), :font-size 20, :font-family "Helvetica"},
    :position {:x 10, :y 0}})))

(defcomp
 comp-tabs
 (tab)
 (container
  {}
  (comp-tab-entry :drafts "Drafts" {:x 10, :y 100} (= :drafts tab))
  (comp-tab-entry :repeated "Repeated" {:x 10, :y 150} (= :repeated tab))
  (comp-tab-entry :tree "Tree" {:x 10, :y 200} (= :tree tab))
  (comp-tab-entry :grids "Grids" {:x 10, :y 250} (= :grids tab))))

(defcomp
 comp-container
 (store)
 (println "Store" store (:tab store))
 (container
  {}
  (comp-tabs (:tab store))
  (case (:tab store)
    :drafts (comp-drafts (:x store))
    :repeated
      (container
       {}
       (text
        {:text "Repeated",
         :position {:x 200, :y 0},
         :style {:fill (hslx 200 80 80), :font-size 20, :font-family "Helvetica"}}))
    :tree (comp-circle-tree)
    :grids (comp-grids)
    (text
     {:text "Unknown",
      :style {:fill (hslx 0 100 80), :font-size 12, :font-family "Helvetica"}}))))
