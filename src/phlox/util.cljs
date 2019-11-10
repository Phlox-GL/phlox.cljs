
(ns phlox.util (:require ["color" :as color]))

(defn hslx [h s l] (-> color (.hsl h s l) (.rgbNumber)))

(defn use-number [x]
  (if (and (number? x) (not (js/isNaN x))) x (do (js/console.error "Invalid number:" x) 0)))
