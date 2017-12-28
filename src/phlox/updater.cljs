
(ns phlox.updater (:require [respo.cursor :refer [mutate]]))

(defn updater [store op op-data]
  (case op
    :states (update store :states (mutate op-data))
    :content (assoc store :content op-data)
    store))
