
(ns phlox.updater )

(defn updater [store op op-data] (case op :content (assoc store :content op-data) store))
