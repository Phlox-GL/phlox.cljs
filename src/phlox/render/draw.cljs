
(ns phlox.render.draw (:require [phlox.util :refer [use-number]]))

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
