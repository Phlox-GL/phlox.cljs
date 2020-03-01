
(ns phlox.app.comp.keyboard
  (:require [phlox.core
             :refer
             [defcomp g hslx rect circle text container graphics create-list]]))

(defcomp
 comp-keyboard
 (on? counted)
 (container
  {:position [400 200]}
  (container
   {:position [0 0]}
   (rect
    {:position [0 0],
     :size [160 40],
     :fill (hslx 0 0 50),
     :on {:click (fn [e d!] (d! :toggle-keyboard nil))}})
   (text
    {:text (str "Toggle: " on?),
     :position [4 8],
     :style {:font-size 16, :fill (hslx 0 0 100)}}))
  (text
   {:text (str "Counted: " counted),
    :position [20 60],
    :style {:font-size 16, :fill (hslx 0 0 100)},
    :on-keyboard (if on?
      {:down (fn [e d!] (d! :counted nil)), :up (fn [e d!] (println :up))}
      {})})))
