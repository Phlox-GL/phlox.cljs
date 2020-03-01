
(ns phlox.comp.slider
  (:require [phlox.core
             :refer
             [defcomp g hslx rect circle text container graphics create-list]]
            [phlox.check :refer [lilac-event-map dev-check]]
            [lilac.core
             :refer
             [record+
              number+
              string+
              optional+
              tuple+
              enum+
              map+
              fn+
              any+
              keyword+
              boolean+
              vector+
              or+
              is+]]))

(defcomp
 comp-slider
 (cursor states props)
 (dev-check cursor (vector+ (or (keyword+) (number+))))
 (dev-check
  props
  (record+
   {:value (number+),
    :unit (number+),
    :on-change (fn+),
    :fill (number+),
    :color (number+),
    :position (tuple+ [(number+) (number+)])}
   {:check-keys? true, :all-optional? true}))
 (let [value (or (:value props) 1)
       state (or (:data states) {:v0 value, :x0 0, :dragging? false})
       unit (or (:unit props) 1)
       fill (or (:fill props) (hslx 0 0 30))
       color (or (:color props) (hslx 0 0 100))
       on-change (:on-change props)]
   (container
    {:position (:position props)}
    (rect
     {:size [140 24],
      :fill fill,
      :on {:mousedown (fn [e d!]
             (let [x1 (-> e .-data .-global .-x)]
               (d! cursor {:dragging? true, :v0 value, :x0 x1}))),
           :mousemove (fn [e d!]
             (when (:dragging? state)
               (let [x2 (-> e .-data .-global .-x)]
                 (if (fn? on-change)
                   (on-change (+ (:v0 state) (* unit (- x2 (:x0 state)))) d!)
                   (js/console.log "[slider] missing :on-change listener"))))),
           :mouseup (fn [e d!] (d! cursor {:v0 value, :x0 0, :dragging? false})),
           :mouseupoutside (fn [e d!] (d! cursor {:v0 value, :x0 0, :dragging? false}))}}
     (text
      {:text (str "◀ " (if (number? value) (.toFixed value 4) "nil") " ▶ " unit),
       :position [4 4],
       :style {:fill color, :font-size 12, :font-family "Menlo, monospace"}})))))
