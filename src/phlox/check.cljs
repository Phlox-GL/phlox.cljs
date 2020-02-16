
(ns phlox.check
  (:require [lilac.core
             :refer
             [record+ number+ string+ optional+ tuple+ map+ fn+ keyword+ vector+ or+]])
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

(def lilac-text
  (record+
   {:text (string+),
    :style (record+
            {:fill (optional+ lilac-color),
             :font-size (optional+ (number+)),
             :font-family (optional+ (string+)),
             :align (optional+ (string+)),
             :font-weight (optional+ (number+))}
            {:check-keys? true}),
    :position (optional+ lilac-point),
    :pivot (optional+ (number+)),
    :rotation (optional+ (number+)),
    :alpha (optional+ (number+))}
   {:check-keys? true}))
