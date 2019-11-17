
(ns phlox.core)

(defmacro defcomp [comp-name args & children]
  `(defn ~comp-name [~@args]
    {:name (keyword `comp-name)
      :phlox-node :component
      :args [~@args]
      :render (fn [~@args] ~@children)}))

(def current-elements '(container rect circle text))

(defn helper-create-element [el props children]
  `(phlox.core/create-element ~el ~props [~@children]))

(defn define-element-fn [el]
  `(defmacro ~el [~'props & ~'children]
     (helper-create-element ~(keyword el) ~'props ~'children)))

(defmacro create-elements []
  `(do ~@(map define-element-fn current-elements)))

(create-elements)