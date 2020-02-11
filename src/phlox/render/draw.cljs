
(ns phlox.render.draw (:require [phlox.util :refer [use-number]]))

(defn add-events [target events dispatch!]
  (when (some? events)
    (set! (.-interactive target) true)
    (set! (.-buttonMode target) true)
    (doseq [[k listener] events]
      (.on target (name k) (fn [event] (listener event dispatch!))))))

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
      :begin-fill (.beginFill target (:color data))
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

(defn draw-circle [target options]
  (if (map? options)
    (.drawCircle
     target
     (use-number (:x options))
     (use-number (:y options))
     (use-number (:radius options)))
    (js/console.warn "Unknown options" options)))

(defn draw-rect [target options]
  (if (map? options)
    (.drawRect
     target
     (use-number (:x options))
     (use-number (:y options))
     (use-number (:width options))
     (use-number (:height options)))
    (js/console.warn "Unknown options" options)))

(defn set-alpha [target alpha] (when (some? alpha) (set! (-> target .-alpha) alpha)))

(defn set-line-style [target line-style]
  (when (some? line-style)
    (.lineStyle
     target
     (use-number (:width line-style))
     (use-number (:color line-style))
     (:alpha line-style))))

(defn set-pivot [target pivot]
  (when-not (nil? pivot)
    (set! (-> target .-pivot .-x) (-> pivot :x))
    (set! (-> target .-pivot .-y) (-> pivot :y))))

(defn set-position [target options]
  (when (some? options)
    (set! (-> target .-position .-x) (-> options :x))
    (set! (-> target .-position .-y) (-> options :y))))

(defn set-rotation [target v] (when (some? v) (set! (.-rotation target) v)))

(defn update-events [target events old-events dispatch!]
  (doseq [[k listener] old-events] (.off target (name k)))
  (doseq [[k listener] events]
    (.on target (name k) (fn [event] (listener event dispatch!))))
  (if (some? events)
    (do (set! (.-buttonMode target) true) (set! (.-interactive target) true))
    (do (set! (.-buttonMode target) false) (set! (.-interactive target) false))))
