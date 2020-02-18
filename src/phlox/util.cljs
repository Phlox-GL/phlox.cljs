
(ns phlox.util (:require [clojure.string :as string] ["pixi.js" :as PIXI]))

(defn camel-case [x] (string/replace x #"-[a-z]" (fn [x] (string/upper-case (subs x 1)))))

(defn component? [x] (= :component (:phlox-node x)))

(defn convert-line-style [props]
  (->> props
       (map
        (fn [[k v]]
          (let [key-name (camel-case (cond (keyword? k) (name k) (string? k) k :else (str k)))]
            (if (= k :fill-gradient-type)
              [key-name
               (case v
                 :h (-> PIXI .-TEXT_GRADIENT .-LINEAR_HORIZONTAL)
                 :horizontal (-> PIXI .-TEXT_GRADIENT .-LINEAR_HORIZONTAL)
                 :v (-> PIXI .-TEXT_GRADIENT .-LINEAR_VERTICAL)
                 :vertical (-> PIXI .-TEXT_GRADIENT .-LINEAR_VERTICAL)
                 (do (println "unknown gradient type:") v))]
              [key-name
               (cond
                 (keyword? v) (name v)
                 (string? v) v
                 (number? v) v
                 (boolean? v) v
                 (vector? v) v
                 :else (do (println "Unknown style value:" v) v))]))))
       (into {})
       (clj->js)))

(defn element? [x] (= :element (:phlox-node x)))

(defn index-items [xs] (->> xs (map-indexed (fn [idx x] [idx x]))))

(defn remove-nil-values [dict] (->> dict (filter (fn [[k v]] (some? v)))))

(defn use-number [x]
  (if (and (number? x) (not (js/isNaN x))) x (do (js/console.error "Invalid number:" x) 0)))
