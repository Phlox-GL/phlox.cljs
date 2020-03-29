
(ns phlox.cursor )

(defn update-states [store op-data]
  (let [[cursor data] op-data] (assoc-in store (concat [:states] cursor [:data]) data)))
