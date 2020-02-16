
(ns phlox.check)

(defmacro dev-check [data rule]
  `(when phlox.check/in-dev?
    (let [result# (lilac.core/validate-lilac ~data ~rule)]
      (when-not (:ok? result#)
        (js/console.error (:formatted-message result#)
          \newline
          (str "(dev-check " '~data " " '~rule ") , where props is:")
          (~'clj->js ~data))))))

(defmacro dev-check-message [message data rule]
  `(when phlox.check/in-dev?
    (let [result# (lilac.core/validate-lilac ~data ~rule)]
      (when-not (:ok? result#)
        (js/console.error (:formatted-message result#)
          \newline
          (str ~message " , when data is:")
          (~'clj->js ~data))))))
