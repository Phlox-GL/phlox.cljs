
(defn read-password [guide]
  (String/valueOf (.readPassword (System/console) guide nil)))

(set-env!
  :resource-paths #{"src"}
  :dependencies '[]
  :repositories #(conj % ["clojars" {:url "https://clojars.org/repo/"
                                     :username "jiyinyiyong"
                                     :password (read-password "Clojars password: ")}]))

(def +version+ "0.1.0")

(deftask deploy []
  (comp
    (pom :project     'quamolit/phlox
         :version     +version+
         :description "Animation libray"
         :url         "https://github.com/Quamolit/phlox"
         :scm         {:url "https://github.com/Quamolit/phlox"}
         :license     {"MIT" "http://opensource.org/licenses/mit-license.php"})
    (jar)
    (push :repo "clojars" :gpg-sign false)))
