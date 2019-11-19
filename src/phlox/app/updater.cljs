
(ns phlox.app.updater )

(defn updater [store op op-data op-id op-time]
  (case op :content (assoc store :content op-data) :hydrate-storage op-data store))
