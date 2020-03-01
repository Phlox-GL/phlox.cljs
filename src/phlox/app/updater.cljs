
(ns phlox.app.updater )

(defn updater [store op op-data op-id op-time]
  (case op
    :add-x (update store :x (fn [x] (if (> x 10) 0 (+ x 1))))
    :tab (assoc store :tab op-data)
    :toggle-keyboard (update store :keyboard-on? not)
    :counted (update store :counted inc)
    :states
      (let [[cursor new-state] op-data]
        (assoc-in store (concat [:states] cursor [:data]) new-state))
    :hydrate-storage op-data
    (do (println "unknown op" op op-data) store)))
