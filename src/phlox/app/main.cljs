
(ns phlox.app.main
  (:require ["pixi.js" :as PIXI]
            [phlox.core :refer [render!]]
            [phlox.app.container :refer [comp-container]]
            [phlox.app.schema :as schema]
            [phlox.app.config :refer [dev?]]
            ["shortid" :as shortid]
            [phlox.app.updater :refer [updater]]
            ["fontfaceobserver" :as FontFaceObserver]))

(defonce *store (atom schema/store))

(defn dispatch! [op op-data]
  (if (vector? op)
    (recur :states [op op-data])
    (do
     (when (and dev? (not= op :states)) (println "dispatch!" op op-data))
     (let [op-id (shortid/generate), op-time (.now js/Date)]
       (reset! *store (updater @*store op op-data op-id op-time))))))

(defn main! []
  (comment js/console.log PIXI)
  (-> (FontFaceObserver. "Josefin Sans")
      (.load)
      (.then (fn [] (render! (comp-container @*store) dispatch! {}))))
  (add-watch *store :change (fn [] (render! (comp-container @*store) dispatch! {})))
  (println "App Started"))

(defn reload! []
  (println "Code updated")
  (render! (comp-container @*store) dispatch! {:swap? true}))
