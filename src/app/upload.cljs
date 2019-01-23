
(ns app.upload
  (:require ["child_process" :as cp] [app.config :as config])
  (:require-macros [clojure.core.strint :refer [<<]]))

(defn sh! [command] (println command) (println (.toString (cp/execSync command))))

(defn main! []
  (sh! (<< "rsync -avr --progress dist/* ~(:cdn-folder config/site)"))
  (sh!
   (<<
    "rsync -avr --progress dist/{index.html,manifest.json} ~(:upload-folder config/site)")))

(defn reload! [] )
