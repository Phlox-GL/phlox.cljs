
(ns phlox.app.updater (:require [phlox.cursor :refer [update-states]]))

(defn updater [store op op-data op-id op-time]
  (case op
    :add-x (update store :x (fn [x] (if (> x 10) 0 (+ x 1))))
    :tab (assoc store :tab op-data)
    :toggle-keyboard (update store :keyboard-on? not)
    :counted (update store :counted inc)
    :states (update-states store op-data)
    :hydrate-storage op-data
    (do (println "unknown op" op op-data) store)))
