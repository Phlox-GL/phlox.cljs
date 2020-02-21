
(ns phlox.render.draw
  (:require [phlox.util :refer [use-number]]
            [lilac.core
             :refer
             [record+
              number+
              string+
              optional+
              boolean+
              tuple+
              map+
              fn+
              keyword+
              vector+
              or+]]
            [phlox.check
             :refer
             [dev-check dev-check-message lilac-point lilac-line-style lilac-color]]))

(defn call-graphics-ops [target ops]
  (doseq [[op data] ops]
    (case op
      :move-to (.moveTo target (first data) (peek data))
      :line-to (.lineTo target (first data) (peek data))
      :line-style
        (.lineStyle
         target
         (use-number (:width data))
         (use-number (:color data))
         (:alpha data))
      :begin-fill (.beginFill target (:color data) (:alpha data))
      :end-fill (.endFill target)
      :close-path (.closePath target)
      :arc
        (let [center (:center data), angle (:angle data)]
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
          (.arcTo target (first p1) (peek p1) (first p2) (peek p2) (:radius data)))
      :bezier-to
        (let [p1 (:p1 data), p2 (:p2 data), to-p (:to-p data)]
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

(defn init-alpha [target alpha] (when (some? alpha) (set! (-> target .-alpha) alpha)))

(defn init-angle [target v] (when (some? v) (set! (.-angle target) v)))

(defn init-events [target events dispatch!]
  (when (some? events)
    (set! (.-interactive target) true)
    (set! (.-buttonMode target) true)
    (doseq [[k listener] events]
      (.on target (name k) (fn [event] (listener event dispatch!))))))

(defn init-line-style [target line-style]
  (when (some? line-style)
    (.lineStyle
     target
     (use-number (:width line-style))
     (use-number (:color line-style))
     (:alpha line-style))))

(defn init-pivot [target pivot]
  (when (some? pivot)
    (set! (-> target .-pivot .-x) (first pivot))
    (set! (-> target .-pivot .-y) (peek pivot))))

(defn init-position [target point]
  (when (some? point)
    (set! (-> target .-position .-x) (if (vector? point) (first point) 0))
    (set! (-> target .-position .-y) (if (vector? point) (peek point) 0))))

(defn init-rotation [target v] (when (some? v) (set! (.-rotation target) v)))

(defn set-alpha [target alpha] (set! (-> target .-alpha) (or alpha 1)))

(defn set-line-style [target line-style]
  (when (some? line-style)
    (.lineStyle
     target
     (use-number (:width line-style))
     (use-number (:color line-style))
     (:alpha line-style))
    (.lineStyle target 0 0 0)))

(defn set-pivot [target pivot]
  (dev-check pivot (optional+ lilac-point))
  (set! (-> target .-pivot .-x) (if (vector? pivot) (first pivot) 0))
  (set! (-> target .-pivot .-y) (if (vector? pivot) (peek pivot) 0)))

(defn set-position [target point]
  (set! (-> target .-position .-x) (if (vector? point) (first point) 0))
  (set! (-> target .-position .-y) (if (vector? point) (peek point) 0)))

(defn set-rotation [target v] (set! (.-rotation target) (or v 0)))

(defn update-events [target events old-events dispatch!]
  (doseq [[k listener] old-events] (.off target (name k)))
  (doseq [[k listener] events]
    (.on target (name k) (fn [event] (listener event dispatch!))))
  (if (some? events)
    (do (set! (.-buttonMode target) true) (set! (.-interactive target) true))
    (do (set! (.-buttonMode target) false) (set! (.-interactive target) false))))
