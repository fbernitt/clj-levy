(ns clj-levy.core-test
  (:require [clojure.test :refer :all]
            [clj-levy.core :refer :all]))

(deftest test-levenshtein
  (is (= 0 (levenshtein "foo" "foo")))
  (is (= 3 (levenshtein "foo" "")))
  (is (= 6 (levenshtein "" "foobar")))
  (is (= 1 (levenshtein "a" "b")))
  (is (= 2 (levenshtein "ab" "bc")))
  (is (= 2 (levenshtein "ab" "ba")))
  (is (= 3 (levenshtein "foo" "bar")))
  (is (= 2 (levenshtein "lein" "lien")))
  (is (= 4 (time (levenshtein "levenshtein" "meilenstein")))))

(deftest test-damerau-levenshtein
  (is (= 0 (damerau-levenshtein "foo" "foo")))
  (is (= 3 (damerau-levenshtein "foo" "")))
  (is (= 6 (damerau-levenshtein "" "foobar")))
  (is (= 1 (damerau-levenshtein "a" "b")))
  (is (= 2 (damerau-levenshtein "ab" "bc")))
  (is (= 1 (damerau-levenshtein "ab" "ba")))
  (is (= 3 (damerau-levenshtein "foo" "bar")))
  (is (= 1 (damerau-levenshtein "lein" "lien")))
  (is (= 4 (time (damerau-levenshtein "levenshtein" "meilenstein")))))

(deftest test-keyboard-distance
  (is (= 0 (keyboard-distance "a" "a")))
  (is (= 1 (keyboard-distance "a" "s")))
  (is (= 8 (keyboard-distance "a" "l")))
  (is (= 1 (keyboard-distance "a" "q")))
  (is (= 2 (keyboard-distance "a" "3")))
  (is (= 12 (keyboard-distance "lein" "lei")))
  (is (= 4 (keyboard-distance "lein" "kwub"))))

(deftest test-keyboard-distance-probability
  (letfn [(round [f] (* 0.01 (Math/round (* 100 f))))]
  (is (= 1.0 (keyboard-distance-probability "foo" "foo")))
  (is (= 0.92 (round (keyboard-distance-probability "test" "rwar"))))
  (is (= 0.88 (round (keyboard-distance-probability "compile" "elipmoc"))))
    ))

