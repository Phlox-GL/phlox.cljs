
(ns phlox.comp.button
  (:require [phlox.core
             :refer
             [defcomp g hslx rect circle text container graphics create-list]]
            [phlox.util :refer [measure-text-width!]]
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
            [phlox.check :refer [lilac-event-map dev-check]]))

(def lilac-button
  (record+
   {:color (number+),
    :fill (number+),
    :text (string+),
    :size (number+),
    :font-family (string+),
    :position (tuple+ [(number+) (number+)]),
    :on lilac-event-map,
    :on-pointerdown (fn+),
    :align-right? (boolean+)}
   {:all-optional? true, :check-keys? true}))

(defcomp
 comp-button
 (props)
 (dev-check props lilac-button)
 (let [button-text (or (:text props) "BUTTON")
       size (or (:font-size props) 14)
       font-family (or (:font-family props) "Josefin Sans")
       fill (or (:fill props) (hslx 0 0 20))
       color (or (:color props) (hslx 0 0 100))
       position (:position props)
       width (+ 16 (measure-text-width! button-text size font-family))
       align-right? (:align-right? props)]
   (container
    {:position (if align-right? [(- (first position) width) (peek position)] position)}
    (rect
     {:fill fill,
      :size [width 32],
      :on (cond
        (some? (:on props)) (:on props)
        (some? (:on-pointerdown props)) {:pointerdown (:on-pointerdown props)}
        :else (do))})
    (text
     {:text button-text,
      :position [8 8],
      :style {:fill color, :font-size size, :font-family font-family}}))))
