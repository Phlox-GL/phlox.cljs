
(ns phlox.comp.drag-point
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
              is+]]
            [phlox.math :refer [v-add]]))

(def lilac-drag-point
  (record+
   {:unit (optional+ (number+)),
    :radius (optional+ (number+)),
    :fill (optional+ (number+)),
    :color (optional+ (number+)),
    :position (tuple+ [(number+) (number+)]),
    :on-change (fn+)}
   {:check-keys? true}))

(defn comp-drag-point [cursor states props]
  (dev-check cursor (vector+ (any+ {:some? true})))
  (dev-check props lilac-drag-point)
  (let [state (or (:data states) {:dragging false, :x0 [0 0]})
        unit (or (:unit props) 1)
        radius (or (:radius props) 3)
        color (or (:color props) (hslx 0 0 100))
        fill (or (:fill props) (hslx 0 0 60))
        on-change (:on-change props)]
    (let [position (:position props)]
      (container
       {:position position}
       (circle
        {:radius radius,
         :position [0 0],
         :fill fill,
         :on {:mousedown (fn [e d!]
                (let [x (-> e .-data .-global .-x), y (-> e .-data .-global .-y)]
                  (d! cursor (merge state {:dragging? true, :x0 [x y], :p0 position})))),
              :mousemove (fn [e d!]
                (when (:dragging? state)
                  (let [x (-> e .-data .-global .-x), y (-> e .-data .-global .-y)]
                    (let [x0 (:x0 state)]
                      (on-change
                       (v-add
                        (:p0 state)
                        [(* unit (- x (first x0))) (* unit (- y (peek x0)))])
                       d!))))),
              :mouseup (fn [e d!] (d! cursor (assoc state :dragging? false))),
              :mouseupoutside (fn [e d!] (d! cursor (assoc state :dragging? false)))}})
       (text
        {:text (str
                "("
                (.toFixed (or (first position) 0) 1)
                ", "
                (.toFixed (or (peek position) 0) 1)
                ")➤"
                (str unit)),
         :alpha 0.3,
         :position [-20 -16],
         :style {:fill color,
                 :font-size 10,
                 :line-height 10,
                 :font-family "Menlo, monospace"}})))))
