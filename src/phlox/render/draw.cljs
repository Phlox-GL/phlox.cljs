
(ns phlox.render.draw
  (:require [phlox.util :refer [use-number]]
            [lilac.core :refer [tuple+ number+ optional+]]
            [phlox.check :refer [dev-check]]
            [lilac.core
             :refer
             [record+
              number+
              string+
              optional+
              tuple+
              map+
              fn+
              keyword+
              or+
              boolean+
              vector+]]))

(defn add-events [target events dispatch!]
  (when (some? events)
    (set! (.-interactive target) true)
    (set! (.-buttonMode target) true)
    (doseq [[k listener] events]
      (.on target (name k) (fn [event] (listener event dispatch!))))))

(def lilac-color (or+ [(number+) (string+)]))

(def lilac-line-style (record+ {:width (number+), :color (number+), :alpha (number+)}))

(def lilac-point (tuple+ [(number+) (number+)]))

(defn call-graphics-ops [target ops]
  (doseq [[op data] ops]
    (case op
      :move-to (do (dev-check data lilac-point) (.moveTo target (first data) (peek data)))
      :line-to (do (dev-check data lilac-point) (.lineTo target (first data) (peek data)))
      :line-style
        (do
         (dev-check data lilac-line-style)
         (.lineStyle
          target
          (use-number (:width data))
          (use-number (:color data))
          (:alpha data)))
      :begin-fill
        (do
         (dev-check
          data
          (record+
           {:color (optional+ lilac-color), :alpha (optional+ (number+))}
           {:check-keys? true}))
         (.beginFill target (:color data)))
      :end-fill (.endFill target)
      :close-path (.closePath target)
      :arc
        (let [center (:center data), angle (:angle data)]
          (dev-check
           data
           (record+
            {:center lilac-point,
             :angle (tuple+ [(number+) (number+)]),
             :radius (number+),
             :anticlockwise? (optional+ (boolean+))}
            {:exact-keys? true}))
          (.arc
           target
           (first center)
           (peek center)
           (:radius data)
           (first angle)
           (peek angle)
           (:anticlockwise? data)))
      :arc-to
        (let [p1 (:p1 data), p2 (:p2 data)]
          (dev-check
           data
           (record+
            {:p1 lilac-point, :p2 lilac-point, :radius (number+)}
            {:exact-keys? true}))
          (.arcTo target (first p1) (peek p1) (first p2) (peek p2) (:radius data)))
      :bezier-to
        (let [p1 (:p1 data), p2 (:p2 data), to-p (:to-p data)]
          (dev-check
           data
           (record+
            {:p1 lilac-point, :p2 lilac-point, :to-p lilac-point}
            {:exact-keys? true}))
          (.bezierCurveTo
           target
           (first p1)
           (peek p1)
           (first p2)
           (peek p2)
           (first to-p)
           (peek to-p)))
      :quadratic-to
        (let [p1 (:p1 data), to-p (:to-p data)]
          (dev-check data (record+ {:p1 lilac-point, :to-p lilac-point} {:exact-keys? true}))
          (.quadraticCurveTo target (first p1) (peek p1) (first to-p) (peek to-p)))
      :begin-hole (.beginHole target)
      :end-hole (.endHole target)
      (js/console.warn "not supported:" op))))

(defn draw-circle [target position radius]
  (if (and (vector? position) (number? radius))
    (.drawCircle
     target
     (use-number (first position))
     (use-number (peek position))
     (use-number radius))
    (js/console.warn "Unknown options" position radius)))

(defn draw-rect [target position size]
  (if (and (vector? position) (vector? size))
    (.drawRect
     target
     (use-number (first position))
     (use-number (peek position))
     (use-number (first size))
     (use-number (peek size)))
    (js/console.warn "Unknown options" position size)))

(defn set-alpha [target alpha] (when (some? alpha) (set! (-> target .-alpha) alpha)))

(defn set-line-style [target line-style]
  (when (some? line-style)
    (.lineStyle
     target
     (use-number (:width line-style))
     (use-number (:color line-style))
     (:alpha line-style))))

(defn set-pivot [target pivot]
  (dev-check pivot (optional+ lilac-point))
  (when-not (nil? pivot)
    (set! (-> target .-pivot .-x) (first pivot))
    (set! (-> target .-pivot .-y) (peek pivot))))

(defn set-position [target point]
  (dev-check point (optional+ (tuple+ [(number+) (number+)])))
  (when (some? point)
    (set! (-> target .-position .-x) (first point))
    (set! (-> target .-position .-y) (peek point))))

(defn set-rotation [target v] (when (some? v) (set! (.-rotation target) v)))

(defn update-events [target events old-events dispatch!]
  (doseq [[k listener] old-events] (.off target (name k)))
  (doseq [[k listener] events]
    (.on target (name k) (fn [event] (listener event dispatch!))))
  (if (some? events)
    (do (set! (.-buttonMode target) true) (set! (.-interactive target) true))
    (do (set! (.-buttonMode target) false) (set! (.-interactive target) false))))
