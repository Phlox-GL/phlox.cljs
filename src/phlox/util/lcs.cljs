
(ns phlox.util.lcs )

(defn find-minimal-ops [state xs ys]
  (comment println "find ops" state (count xs) (count ys))
  (cond
    (and (empty? xs) (empty? ys)) state
    (and (empty? xs) (not (empty? ys)))
      (recur
       (-> state (update :acc (fn [acc] (conj acc [:add (first ys)]))) (update :step inc))
       []
       (rest ys))
    (and (empty? ys) (not (empty? xs)))
      (recur
       (-> state
           (update :acc (fn [acc] (conj acc [:remove (first xs)])))
           (update :step inc))
       (rest xs)
       [])
    :else
      (let [x0 (first xs), y0 (first ys)]
        (cond
          (= x0 y0)
            (recur
             (-> state (update :acc (fn [acc] (conj acc [:remains x0]))) (update :step inc))
             (rest xs)
             (rest ys))
          (nil? (some (fn [y] (= x0 y)) ys))
            (recur
             (-> state (update :acc (fn [acc] (conj acc [:remove x0]))) (update :step inc))
             (rest xs)
             ys)
          (nil? (some (fn [x] (= y0 x)) xs))
            (recur
             (-> state (update :acc (fn [acc] (conj acc [:add y0]))) (update :step inc))
             xs
             (rest ys))
          :else
            (let [solution-a (find-minimal-ops
                              (-> state
                                  (update :acc (fn [acc] (conj acc [:remove (first xs)])))
                                  (update :step inc))
                              (rest xs)
                              ys)
                  solution-b (find-minimal-ops
                              (-> state
                                  (update :acc (fn [acc] (conj acc [:add (first ys)])))
                                  (update :step inc))
                              xs
                              (rest ys))]
              (if (<= (:step solution-a) (:step solution-b)) solution-a solution-b))))))

(def lcs-state-0 {:acc [], :step 0})
