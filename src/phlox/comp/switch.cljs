
(ns phlox.comp.switch
  (:require [phlox.core
             :refer
             [defcomp g hslx rect circle text container graphics create-list]]
            [phlox.check :refer [lilac-event-map dev-check lilac-point]]
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

(def lilac-switch
  (record+
   {:value (boolean+),
    :position (optional+ lilac-point),
    :on-change (fn+),
    :title (optional+ (string+))}
   {:check-keys? true}))

(defcomp
 comp-switch
 (props)
 (dev-check props lilac-switch)
 (let [value (:value props), on-change (:on-change props)]
   (container
    {:position (or (:position props) [0 0])}
    (rect
     {:size [56 20],
      :fill (if value (hslx 0 0 92) (hslx 0 0 50)),
      :position [0 0],
      :radius 3,
      :on {:click (fn [e d!] (when (fn? on-change) (on-change (not value) d!)))}})
    (text
     {:text (if value "On" "Off"),
      :position (if value [8 2] [24 2]),
      :style {:font-size 14,
              :fill (if value (hslx 0 0 50) (hslx 0 0 100)),
              :font-family "Arial",
              :align :right,
              :font-weight 500},
      :alpha (if value 1 0.4)})
    (text
     {:text (or (:title props) "Switch"),
      :position [0 -20],
      :style {:fill (hslx 0 0 80), :font-size 13, :font-family "Arial, sans-serif"},
      :alpha 1}))))
