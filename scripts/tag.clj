
(defn create-element [x y z])

(def current-elements '(container rect circle text graphics))

(defn helper-create-element [el props children]
  `(create-element ~el ~props [~@children]))

(defn define-element-fn [el]
  `(defmacro ~el [~'props & ~'children]
     (helper-create-element ~(keyword el) ~'props ~'children)))

(defmacro create-elements []
  `(do ~@(map define-element-fn current-elements)))

(create-elements)

(println (macroexpand '(rect {:a 1} 1 2 3)))
; (println (rect {:a 1} 1 2 3))
