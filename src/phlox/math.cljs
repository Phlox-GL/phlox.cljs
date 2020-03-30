
(ns phlox.math )

(def radian-ratio (/ js/Math.PI 180))

(defn angle->radian [x] (* x radian-ratio))

(defn v-add [[a b] [x y]] [(+ a x) (+ b y)])
