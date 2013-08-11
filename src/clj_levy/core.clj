(ns clj-levy.core
  (:import (clj_levy Levenshtein DamerauLevenshtein)))

(defn- recursive-levenshtein
  "Calculates the levenshtein distance for two strings recursivly. Implementation gets very slow for longer strings."
  [seq1 seq2]
  (cond
    (empty? seq1) (count seq2)
    (empty? seq2) (count seq1)
    :else (min
            (+ (if (= (first seq1) (first seq2)) 0 1)
               (recursive-levenshtein (rest seq1) (rest seq2)))
            (inc (recursive-levenshtein (rest seq1) seq2))
            (inc (recursive-levenshtein seq1 (rest seq2))))))


(defn levenshtein
  "Calculates the levenshtein distance for two strings recursivly."
  [seq1 seq2]
  (Levenshtein/distanceOf seq1 seq2))

(defn damerau-levenshtein
  "Calculates the damerau-levenshtein distance of two strings in an iterative way"
  [seq1 seq2]
  (DamerauLevenshtein/distanceOf seq1 seq2))


; http://stackoverflow.com/questions/8619785/what-is-an-efficient-way-to-measure-similarity-between-two-strings-levenshtein
(defn- clojure-damerau-levenshtein
  "Calculates the damerau-levenshtein distance of two strings in an iterative way. Iterative clojure implementation using arrays."
  [seq1 seq2]
  (let [score (make-array Integer/TYPE (+ 2 (count seq1)) (+ 2 (count seq2)))]
    (dotimes [row (inc (count seq1))]
      (aset-int score row 0 row))
    (dotimes [col (inc (count seq2))]
      (aset-int score 0 col col))
    (doseq [y (range 1 (inc (count seq1)))]
      (doseq [x (range 1 (inc (count seq2)))]
        (let [cost (if (= (nth seq1 (dec y)) (nth seq2 (dec x))) 0 1)
              value (min 
                      (inc (aget score (dec y) x))
                      (inc (aget score y (dec x)))
                      (+ cost (aget score (dec y) (dec x))))]
          (aset-int score y x value)
          ;now the damerau additions
          (when (and (> x 1) (> y 1))
            (when (and (= (nth seq1 (dec y)) (nth seq2 (- x 2)))
                     (= (nth seq1 (- y 2)) (nth seq2 (dec x))))
              (let [value (min
                            (aget score y x)
                            (+ cost (aget score (- y 2) (- x 2))))]
                (aset-int score y x value))))
      )))
    (aget score (count seq1) (count seq2))))

(def qwertz-chars [["^1234567890ß´" "°!\"§$%&/()=?`"]
                   [" qwertzuiopü+" " QWERTZUIOPÜ*"]
                   [" asdfghjklöä#" " ASDFGHJKLÖÄ'"]
                   [" <yxcvbnm,.- " " >YXCVBNM;:_ "]])

(def qwerty-chars [["§1234567890-=" "±!@#$%^&*()_+"]
                   [" qwertyuiop[]" " QWERTYUIOP{}"]
                   [" asdfghjkl;'\\" " ASDFGHJKL:\"|"]
                   [" `zxcvbnm,./ " " ~ZXCVBNM<>? "]])

(defn build-key-map
  [keyboard-layout]
  (into {} (reduce into (reduce into 
    (map-indexed (fn [row buttons-shifted]
                   (map-indexed (fn [shift buttons]
                                  (map-indexed (fn [col button] 
                                                 [button [col row shift]]) buttons))
                                buttons-shifted))
                 keyboard-layout)))))

(defn- button-distance
  "Calculates the distance between two keys - destructures keymap [col row shift]"
  [keymap [x1 y1 shift1] [x2 y2 shift2]]
  (let [x (- x2 x1)
        y (- y2 y1)]
    (int (Math/sqrt (+ (* x x) (* y y))))))

(defn- max-distance
  "Calculates the maximum possible distance between any two keys within the keymap"
  [keymap]
  (let [buttons (vals keymap)]
    (reduce max
      (flatten
        (for [b buttons]
          (map #(button-distance keymap % b) buttons))))))

(defn- char-distance
  "Returns the number of steps between to keys. If no key for char is found max distance is returned"
  [c1 c2 keymap]
  (let [b1 (keymap c1)
        b2 (keymap c2)]
    (if (some nil? [b1 b2])
      13
      (button-distance keymap b1 b2))))

; http://cpansearch.perl.org/src/KRBURTON/String-KeyboardDistance-1.01/KeyboardDistance.pm
; https://gist.github.com/ck/960716
(defn keyboard-distance
  "Returns the distance of two string based on the location of their characters on a keyboard."
  [seq1 seq2]
  (let [keymap (build-key-map qwertz-chars)
        len-dist (* (Math/abs (- (count seq1) (count seq2))) (max-distance keymap))]
    (letfn [(distance [c1 c2] (char-distance c1 c2 keymap))]
      (+ (apply + (map distance seq1 seq2)) len-dist)
      )))


(defn keyboard-distance-probability
  "Returns the probability that two strings are keyboard layout transpositions"
  [str1 str2]
  (let [keymap (build-key-map qwertz-chars)
        short (min (count str1) (count str2))
        long (max (count str1) (count str2))
        dist (keyboard-distance str1 str2)]
        (- 1.0 (/ dist (* long (max-distance keymap))))))

