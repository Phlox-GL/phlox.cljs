
(ns phlox.core)

(defmacro defcomp [x params & body]
  (assert (symbol? x) "1st argument should be a symbol")
  (assert (coll? params) "2nd argument should be a collection")
  (assert (some? (last body)) "defcomp should return something")
  (let [com-helper (gensym (str x "-helper-"))]
    `(do
       (defn ~com-helper [~@params] ~@body)
       (defn ~x [~@params] (phlox.core/call-comp-helper ~com-helper [~@params])))))
