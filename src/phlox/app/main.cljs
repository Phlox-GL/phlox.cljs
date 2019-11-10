
(ns phlox.app.main
  (:require ["pixi.js" :as PIXI]
            [phlox.core :refer [render!]]
            [phlox.app.container :refer [comp-container]]))

(defn main! []
  (comment js/console.log PIXI)
  (render! (comp-container))
  (println "App Started"))

(defn reload! []
  (println "Code updated")
  (comment render! (comp-container))
  (js/location.reload))
