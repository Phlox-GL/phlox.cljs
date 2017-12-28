
(ns phlox.main
  (:require [phlox.updater :refer [updater]]
            [phlox.schema :as schema]
            [verbosely.core :refer [log!]]))

(defn dispatch! [op op-data] )

(defn main! [] (println "App started."))

(def mount-target (.querySelector js/document ".app"))

(defn reload! [] (println "Code updated."))

(set! (.-onload js/window) main!)
