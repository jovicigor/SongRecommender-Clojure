(ns machine-learning.similarity
  (:require [clojure.string :as str]
            [clojure.set :as sets]))

(defn euclidean-distance [first-vector second-vector]
  (Math/sqrt
    (reduce +
            (map #(Math/pow (- %1 %2) 2)
                 first-vector
                 second-vector))))

(defn euclidean-distance-for-features [map1 map2 features]
  (euclidean-distance (map #(% map1)
                           features)
                      (map #(% map2)
                           features)))

(defn find-closest-from-items [item items distance features]
  (first
    (sort-by #(distance % item features)
             items)))

(defn find-closest-different-from-items [item items distance features]
  (let [filtered-items (filter #(not= (:remote_id item)
                                      (:remote_id %)) items)]
    (first
      (sort-by #(distance % item features)
               filtered-items))))

(defn euclidean-similarity-for-features [map1 map2 features]
  (double
    (/
      1
      (+ 1 (euclidean-distance-for-features map1 map2 features)))))

(defn tokenize [genres-string]
  (set
    (str/split (str/trim genres-string)
               #"(-| )")))

(defn cant-tokenize [item1 item2 feature]
  (and
    (empty? (feature item1))
    (empty? (feature item2))))

;tokenizable = can be split into words
(defn similarity-for-tokenizable-feature [item1 item2 feature-to-tokenize]
  (if (cant-tokenize item1 item2 feature-to-tokenize)
    0
    (let [tokens1 (tokenize (feature-to-tokenize item1))
          tokens2 (tokenize (feature-to-tokenize item2))
          all-tokens (sets/union tokens1 tokens2)
          common-tokens (sets/intersection tokens1 tokens2)
          result (double (/ (count common-tokens) (count all-tokens)))]
      result)))


(defn overal-similarity [item1 item2 numeric-features tokenizable-feature]
  (let [euclidean-similarity (euclidean-similarity-for-features item1
                                                                item2
                                                                numeric-features)
        genre-similarity (similarity-for-tokenizable-feature item1
                                                             item2
                                                             tokenizable-feature)]
    (+ euclidean-similarity genre-similarity)))

(defn find-top-similar [item items features tokenizable-feature num-of-matches]
  (let [filtered-items (filter #(not= (:remote_id item) (:remote_id %))
                               items)
        test (reverse
               (sort-by #(overal-similarity % item features tokenizable-feature)
                        filtered-items))]
    (take num-of-matches test)))
