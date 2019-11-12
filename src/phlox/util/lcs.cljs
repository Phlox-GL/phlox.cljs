
(ns phlox.util.lcs )

(defn find-minimal-ops [state xs ys]
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
        (if (= x0 y0)
          (recur
           (-> state (update :acc (fn [acc] (conj acc [:remains x0]))))
           (rest xs)
           (rest ys))
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
