
(ns phlox.check
  (:require [lilac.core
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
              is+]])
  (:require-macros [phlox.check]))

(def in-dev? (do ^boolean js/goog.DEBUG))

(def lilac-line-style (record+ {:width (number+), :color (number+), :alpha (number+)}))

(def lilac-point (tuple+ [(number+) (number+)]))

(def lilac-circle
  (record+
   {:line-style (optional+ lilac-line-style),
    :on (optional+ (map+ (keyword+) (fn+))),
    :position lilac-point,
    :radius (number+),
    :fill (number+),
    :alpha (optional+ (number+)),
    :rotation (optional+ (number+))}
   {:check-keys? true}))

(def lilac-color (or+ [(number+) (string+)]))

(def lilac-container
  (record+
   {:position (optional+ lilac-point),
    :rotation (optional+ (number+)),
    :pivot (optional+ lilac-point),
    :alpha (optional+ (number+))}
   {:check-keys? true}))

(def lilac-graphics
  (record+
   {:on (optional+ (map+ (keyword+) (fn+))),
    :position (optional+ lilac-point),
    :pivot (optional+ lilac-point),
    :alpha (optional+ (number+)),
    :rotation (optional+ (number+)),
    :ops (vector+ (tuple+ [(keyword+)]))}
   {:check-keys? true}))

(def lilac-rect
  (record+
   {:line-style (optional+ lilac-line-style),
    :on (optional+ (map+ (keyword+) (fn+))),
    :position (optional+ lilac-point),
    :size (optional+ lilac-point),
    :pivot (optional+ lilac-point),
    :alpha (optional+ (number+)),
    :rotation (optional+ (number+)),
    :fill (optional+ lilac-color)}
   {:check-keys? true}))

(def lilac-text-style
  (record+
   {:align (enum+ #{:left :center :right}),
    :break-words (boolean+),
    :drop-shadow (boolean+),
    :drop-shadow-alpha (number+ {:min 0, :max 1}),
    :drop-shadow-angle (number+),
    :drop-shadow-blur (number+),
    :drop-shadow-color lilac-color,
    :drop-shadow-distance (number+),
    :fill (or+ [lilac-color (vector+ lilac-color)]),
    :fill-gradient-type (enum+ #{:vertical :horizontal :v :h}),
    :fill-gradient-stops (any+),
    :font-family (string+),
    :font-size (number+),
    :font-style (enum+ #{:normal :italic :oblique}),
    :font-variant (enum+ #{:normal :small-caps}),
    :font-weight (number+),
    :leading (number+),
    :letter-spacing (number+),
    :line-height (number+),
    :line-join (enum+ #{:miter :round :bevel}),
    :miter-limit (number+),
    :padding (number+),
    :stroke lilac-color,
    :stroke-thickness (number+),
    :trim (boolean+),
    :text-baseline (enum+ #{:alphabetic}),
    :white-space (enum+ #{:normal :pre :pre-line}),
    :word-wrap (boolean+),
    :word-wrap-width (number+)}
   {:check-keys? true, :all-optional? true}))

(def lilac-text
  (record+
   {:text (string+),
    :style lilac-text-style,
    :position (optional+ lilac-point),
    :pivot (optional+ (number+)),
    :rotation (optional+ (number+)),
    :alpha (optional+ (number+))}
   {:check-keys? true}))
