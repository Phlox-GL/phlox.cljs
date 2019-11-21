
(ns phlox.render.expand )

(defn expand-tree [element]
  (case (:phlox-node element)
    :component (assoc element :tree (expand-tree (apply (:render element) (:args element))))
    :element
      (update
       element
       :children
       (fn [children-dict]
         (->> children-dict (map (fn [[k child]] [k (expand-tree child)])))))
    (do (js/console.log "Unknown element:" element))))
