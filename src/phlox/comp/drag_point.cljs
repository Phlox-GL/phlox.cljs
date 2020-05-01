
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

(def lilac-cursor (vector+ (any+ {:some? true})))

(def lilac-drag-point
  (record+
   {:unit (optional+ (number+)),
    :title (optional+ (string+)),
    :radius (optional+ (number+)),
    :fill (optional+ (number+)),
    :color (optional+ (number+)),
    :position (tuple+ [(number+) (number+)]),
    :hide-text? (optional+ (boolean+)),
    :on-change (fn+)}
   {:check-keys? true}))

(defn comp-drag-point [states props]
  (dev-check (:cursor states) lilac-cursor)
  (dev-check props lilac-drag-point)
  (let [cursor (:cursor states)
        state (or (:data states) {:dragging? false, :x0 [0 0]})
        unit (or (:unit props) 1)
        radius (or (:radius props) 3)
        color (or (:color props) (hslx 0 0 100))
        fill (or (:fill props) (hslx 0 0 60))
        on-change (:on-change props)
        hide-text? (or (:hide-text? props) false)]
    (let [position (:position props)]
      (container
       {:position position}
       (circle
        {:radius radius,
         :position [0 0],
         :fill fill,
         :on {:pointerdown (fn [e d!]
                (let [x (-> e .-data .-global .-x), y (-> e .-data .-global .-y)]
                  (d! cursor (merge state {:dragging? true, :x0 [x y], :p0 position})))),
              :pointermove (fn [e d!]
                (when (:dragging? state)
                  (let [x (-> e .-data .-global .-x), y (-> e .-data .-global .-y)]
                    (let [x0 (:x0 state)]
                      (on-change
                       (v-add
                        (:p0 state)
                        [(* unit (- x (first x0))) (* unit (- y (peek x0)))])
                       d!))))),
              :pointerup (fn [e d!] (d! cursor (assoc state :dragging? false))),
              :pointerupoutside (fn [e d!] (d! cursor (assoc state :dragging? false)))}})
       (if-not hide-text?
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
                   :font-family "Menlo, monospace"}}))
       (if (and (not hide-text?) (some? (:title props)))
         (text
          {:text (:title props),
           :alpha 0.3,
           :position [-12 6],
           :style {:fill color,
                   :font-size 10,
                   :line-height 10,
                   :font-family "Menlo, monospace",
                   :align :center}}))))))
