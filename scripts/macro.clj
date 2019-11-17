
(defmacro defcomp [comp-name args & children]
  `(defn ~comp-name [~@args]
    {:name (keyword `comp-name)
      :phlox-node :component
      :args [~@args]
      :render (fn [~@args] ~@children)}))

(println (macroexpand-1 '(defcomp comp-a [a b] (println a b))))
