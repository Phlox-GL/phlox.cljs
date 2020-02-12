
(ns phlox.check)

(defmacro dev-check [data rule]
  `(when phlox.check/in-dev?
    (let [result# (lilac.core/validate-lilac ~data ~rule)]
      (when-not (:ok? result#) (js/console.error (:formatted-message result#))))))
