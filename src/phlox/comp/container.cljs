
(ns phlox.comp.container
  (:require [hsl.core :refer [hsl]]
            [respo-ui.style :as ui]
            [respo.macros :refer [defcomp cursor-> <> div button textarea span]]
            [verbosely.core :refer [verbosely!]]
            [respo.comp.space :refer [=<]]
            [reel.comp.reel :refer [comp-reel]]))

(defcomp
 comp-container
 (reel)
 (let [store (:store reel), states (:states store)]
   (div
    {:style (merge ui/global ui/row)}
    (textarea
     {:value (:content store),
      :placeholder "Content",
      :style (merge ui/textarea {:width 640, :height 320}),
      :on {:input (fn [e d! m!] (d! :content (:value e)))}})
    (=< "8px" nil)
    (div
     {}
     (button
      {:style ui/button,
       :inner-text (str "run"),
       :on {:click (fn [e d! m!] (println (:content store)))}}))
    (cursor-> :reel comp-reel states reel {}))))
