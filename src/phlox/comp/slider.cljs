
(ns phlox.comp.slider
  (:require [phlox.core
             :refer
             [defcomp g hslx rect circle text container graphics create-list]]))

(defcomp comp-slider (props) (text {:text "DEMO", :style {:fill (hslx 0 0 100)}}))
