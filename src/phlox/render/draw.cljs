
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
      :move-to (.moveTo target (:x data) (:y data))
      :line-to (.lineTo target (:x data) (:y data))
      :line-style
        (.lineStyle
         target
         (use-number (:width data))
         (use-number (:color data))
         (:alpha data))
      :begin-fill (.beginFill target (:color data))
      :end-fill (.endFill target)
      :close-path (.closePath target)
      :arc (println "TODO")
      :arc-to (println "TODO")
      (js/console.warn "not supported:" op))))

(defn set-alpha [target alpha] (when (some? alpha) (set! (-> target .-alpha) alpha)))

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
  (when (some? old-events) (doseq [[k listener] events] (.off target (name k))))
  (when (some? events)
    (doseq [[k listener] events]
      (.on target (name k) (fn [event] (listener event dispatch!)))))
  (if (some? events)
    (do (set! (.-buttonMode target) true) (set! (.-buttonMode target) true))
    (do (set! (.-buttonMode target) false) (set! (.-buttonMode target) false))))
