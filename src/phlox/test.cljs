
(ns phlox.test
  (:require [cljs.test :refer [deftest is testing run-tests]]
            [phlox.util.lcs :refer [find-minimal-ops lcs-state-0]]))

(deftest
 test-lcs
 (testing
  "Find simple changes"
  (is
   (=
    (find-minimal-ops lcs-state-0 (list "a") (list "b"))
    {:acc [[:remove "a"] [:add "b"]], :step 2}))
  (is
   (= (find-minimal-ops lcs-state-0 (list "a") (list "a")) {:acc [[:remains "a"]], :step 0}))
  (is (= (find-minimal-ops lcs-state-0 (list) (list "a")) {:acc [[:add "a"]], :step 1}))
  (is
   (=
    (find-minimal-ops lcs-state-0 (list "a" "b" "c") (list "a" "c"))
    {:acc [[:remains "a"] [:remove "b"] [:remains "c"]], :step 1}))
  (is
   (=
    (find-minimal-ops lcs-state-0 (list "a" "b" "c") (list "a" "c" "c"))
    {:acc [[:remains "a"] [:remove "b"] [:remains "c"] [:add "c"]], :step 2}))
  (is
   (=
    (find-minimal-ops lcs-state-0 (list "a" "c") (list "a" "b1" "b2" "b3" "c"))
    {:acc [[:remains "a"] [:add "b1"] [:add "b2"] [:add "b3"] [:remains "c"]], :step 3}))))
