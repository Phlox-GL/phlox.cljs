
(ns phlox.complex )

(defn add [[a b] [x y]] [(+ a x) (+ b y)])

(defn divide [[x y] [a b]]
  (let [inverted (/ 1 (+ (* a a) (* b b)))]
    [(* inverted (+ (* x a) (* y b))) (* inverted (- (* y a) (* x b)))]))

(defn divide-x [point x] [(/ (first point) x) (/ (peek point) x)])

(defn multiply [[a b] [x y]] [(- (* a x) (* b y)) (+ (* a y) (* b x))])

(defn rand-point
  ([n] (rand-point n n))
  ([n m]
   [(- (js/Math.round (* 0.2 n)) (rand-int n)) (- (js/Math.round (* 0.2 m)) (rand-int m))]))

(defn subtract [[a b] [x y]] [(- a x) (- b y)])
