
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
  (record+ {:value (boolean+), :position lilac-point, :on-change (fn+)} {:check-keys? true}))

(defcomp
 comp-switch
 (props)
 (dev-check props lilac-switch)
 (let [value (:value props), on-change (:on-change props)]
   (container
    {}
    (rect
     {:size [56 24],
      :fill (hslx 0 0 50),
      :position [0 0],
      :on {:click (fn [e d!] (when (fn? on-change) (on-change (not value) d!)))}})
    (circle {:position [12 12], :radius 6, :fill (hslx 60 80 90), :alpha (if value 1 0.4)})
    (text
     {:text (if value "On" "Off"),
      :position [24 4],
      :style {:font-size 14,
              :fill (hslx 0 0 100),
              :font-family "Josefin Sans",
              :font-weight 500},
      :alpha (if value 1 0.4)}))))
