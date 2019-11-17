
(ns phlox.util (:require ["color" :as color] [clojure.string :as string]))

(defn component? [x] (= :component (:phlox-node x)))

(defn element? [x] (= :element (:phlox-node x)))

(defn hslx [h s l] (-> color (.hsl h s l) (.rgbNumber)))

(defn index-items [xs] (->> xs (map-indexed (fn [idx x] [idx x]))))

(defn kebab-case [x] (string/replace x #"-[a-z]" (fn [x] (string/lower-case (subs x 1)))))

(defn map-to-object [props]
  (->> props
       (map
        (fn [[k v]]
          [(kebab-case (cond (keyword? k) (name k) (string? k) k :else (str k))) v]))
       (into {})
       (clj->js)))

(defn remove-nil-values [dict] (->> dict (filter (fn [[k v]] (some? v)))))

(defn use-number [x]
  (if (and (number? x) (not (js/isNaN x))) x (do (js/console.error "Invalid number:" x) 0)))
