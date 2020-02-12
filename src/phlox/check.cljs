
(ns phlox.check
  (:require [lilac.core :refer [validate-lilac]])
  (:require-macros [phlox.check]))

(def in-dev? (do ^boolean js/goog.DEBUG))
