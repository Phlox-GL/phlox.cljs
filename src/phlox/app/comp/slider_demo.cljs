
(ns phlox.app.comp.slider-demo
  (:require [phlox.core
             :refer
             [defcomp g hslx rect circle text container graphics create-list]]
            [phlox.comp.slider :refer [comp-slider]]))

(defcomp
 comp-slider-demo
 (cursor states)
 (let [state (or (:data states) {:a 40, :b 20, :c 10, :d 10, :e 10, :f 10})]
   (container
    {:position [300 100]}
    (comp-slider
     (conj cursor :a)
     (:a states)
     {:value (:a state),
      :unit 1,
      :position [20 0],
      :on-change (fn [value d!] (d! cursor (assoc state :a value)))})
    (comp-slider
     (conj cursor :b)
     (:b states)
     {:value (:b state),
      :title "Refine",
      :unit 0.1,
      :position [20 60],
      :on-change (fn [value d!] (d! cursor (assoc state :b value)))})
    (comp-slider
     (conj cursor :c)
     (:c states)
     {:value (:c state),
      :unit 10,
      :position [20 120],
      :fill (hslx 50 90 70),
      :color (hslx 200 90 30),
      :on-change (fn [value d!] (d! cursor (assoc state :c value)))})
    (comp-slider
     (conj cursor :d)
     (:d states)
     {:value (:d state),
      :position [20 180],
      :on-change (fn [value d!] (d! cursor (assoc state :d value))),
      :title "Round",
      :round? true})
    (comp-slider
     (conj cursor :e)
     (:e states)
     {:value (:e state),
      :position [20 240],
      :on-change (fn [value d!] (d! cursor (assoc state :e value))),
      :title "min 10",
      :min 10})
    (comp-slider
     (conj cursor :f)
     (:f states)
     {:value (:f state),
      :position [20 300],
      :on-change (fn [value d!] (d! cursor (assoc state :f value))),
      :title "max 10",
      :max 10}))))
