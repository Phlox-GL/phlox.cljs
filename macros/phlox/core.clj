
(ns phlox.core)

(defmacro defcomp [comp-name args & children]
  `(defn ~comp-name [~@args]
    {:name ~(keyword comp-name)
      :phlox-node :component
      :args [~@args]
      :render (fn [~@args] ~@children)}))
