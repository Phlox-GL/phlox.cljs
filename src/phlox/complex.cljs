
(ns phlox.complex )

(defn add [[a b] [x y]] [(+ a x) (+ b y)])

(defn divide-by [point x] [(/ (first point) x) (/ (peek point) x)])

(defn minus [[a b] [x y]] [(- a x) (- b y)])

(defn rand-point
  ([n] (rand-point n n))
  ([n m] [(- n (rand-int (* 2 n))) (- m (rand-int (* 2 m)))]))

(defn rebase [[x y] [a b]]
  "complex number division, renamed since naming collision"
  (let [inverted (/ 1 (+ (* a a) (* b b)))]
    [(* inverted (+ (* x a) (* y b))) (* inverted (- (* y a) (* x b)))]))

(defn times [[a b] [x y]] [(- (* a x) (* b y)) (+ (* a y) (* b x))])
