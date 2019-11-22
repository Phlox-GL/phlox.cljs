
(ns phlox.app.main
  (:require ["pixi.js" :as PIXI]
            [phlox.core :refer [render!]]
            [phlox.app.container :refer [comp-container]]
            [phlox.app.schema :as schema]))

(defonce *store (atom schema/store))

(defn updater [store op op-data]
  (case op
    :add-x (update store :x (fn [x] (if (> x 10) 0 (+ x 1))))
    :tab (assoc store :tab op-data)
    (do (println "unknown op" op op-data) store)))

(defn dispatch! [op op-data]
  (println "dispatch!" op op-data)
  (reset! *store (updater @*store op op-data)))

(defn main! []
  (comment js/console.log PIXI)
  (render! (comp-container @*store) dispatch! {})
  (add-watch *store :change (fn [] (render! (comp-container @*store) dispatch! {})))
  (println "App Started"))

(defn reload! []
  (println "Code updated")
  (render! (comp-container @*store) dispatch! {:swap? true}))
