
(ns phlox.check
  (:require [lilac.core
             :refer
             [record+
              number+
              string+
              optional+
              tuple+
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
   {:align (optional+ (or+ [(is+ :left) (is+ :center) (is+ :right)])),
    :break-words (optional+ (boolean+)),
    :drop-shadow (optional+ (boolean+)),
    :drop-shadow-alpha (optional+ (number+ {:min 0, :max 1})),
    :drop-shadow-angle (optional+ (number+)),
    :drop-shadow-blur (optional+ (number+)),
    :drop-shadow-color (optional+ lilac-color),
    :drop-shadow-distance (optional+ (number+)),
    :fill (optional+ lilac-color),
    :fill-gradient-type (optional+ (any+)),
    :fill-gradient-stops (optional+ (any+)),
    :font-family (optional+ (string+)),
    :font-size (optional+ (number+)),
    :font-style (optional+ (or+ [(is+ :normal) (is+ :italic) (is+ :oblique)])),
    :font-variant (optional+ (or+ [(is+ :normal) (is+ :small-caps)])),
    :font-weight (optional+ (number+)),
    :leading (optional+ (number+)),
    :letter-spacing (optional+ (number+)),
    :line-height (optional+ (number+)),
    :line-join (optional+ (or+ [(is+ :miter) (is+ :round) (is+ :round) (is+ :bevel)])),
    :miter-limit (optional+ (number+)),
    :padding (optional+ (number+)),
    :stroke (optional+ lilac-color),
    :stroke-thickness (optional+ (number+)),
    :trim (optional+ (boolean+)),
    :text-baseline (optional+ (or+ [(is+ :alphabetic)])),
    :white-space (optional+ (or+ [(is+ :normal) (is+ :pre) (is+ :pre-line)])),
    :word-wrap (optional+ (boolean+)),
    :word-wrap-width (optional+ (number+))}
   {:check-keys? true}))

(def lilac-text
  (record+
   {:text (string+),
    :style lilac-text-style,
    :position (optional+ lilac-point),
    :pivot (optional+ (number+)),
    :rotation (optional+ (number+)),
    :alpha (optional+ (number+))}
   {:check-keys? true}))
