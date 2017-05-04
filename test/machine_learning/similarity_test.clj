(ns machine-learning.similarity-test
  (:require [clojure.test :refer :all]
            [machine-learning.similarity :refer :all]))

(defn contains-all? [collection1 collection2]
  (every? #(.contains collection1 %) collection2))

(defn equal-collections? [col1 col2]
  (and
    (contains-all? col1 col2)
    (= (count col1) (count col2))))

(deftest euclidean-distance-test
  (testing "Euclidean distance"
    (let [expected-value 5.0
          vector1 [-2 2]
          vector2 [2 -1]]
      (is
        (= expected-value (euclidean-distance vector1 vector2))))))

(deftest euclidean-distance-for-features-test
  (testing "Euclidean distance only for specified features"
    (let [expected-value 5.0
          item1 {:remote_id "remoteid1" :feature1 -2 :feature2 2 :feature3 15}
          item2 {:remote_id "remoteid2" :feature1 2 :feature2 -1 :feature3 1100}
          features-to-consider '(:feature1 :feature2)]
      (is
        (= expected-value (euclidean-distance-for-features item1
                                                           item2
                                                           features-to-consider))))))
(deftest find-closest-from-items-test
  (testing "Closest from items only for specified features"
    (let [closest-id "remoteid1"
          item {:remote_id "remoteid0" :feature 7}
          data '({:remote_id "remoteid1" :feature 10}
                  {:remote_id "remoteid2" :feature 11}
                  {:remote_id "remoteid3" :feature 12})
          features-to-consider '(:feature)]
      (is
        (=
          closest-id
          (:remote_id (find-closest-from-items item
                                               data
                                               euclidean-distance-for-features
                                               features-to-consider)))))))

(deftest find-closest-different-from-items-test
  (testing "Closest from items only for specified features"
    (let [closest-id "remoteid2"
          item {:remote_id "remoteid1" :feature 10}
          data '({:remote_id "remoteid1" :feature 10}
                  {:remote_id "remoteid2" :feature 11}
                  {:remote_id "remoteid3" :feature 12})
          features-to-consider '(:feature)]
      (is
        (=
          closest-id
          (:remote_id (find-closest-different-from-items item
                                                         data
                                                         euclidean-distance-for-features
                                                         features-to-consider)))))))

(deftest euclidean-similarity-for-features-test
  (testing "Euclidean similarity only for specified features"
    (let [expected-value 0.16666666666666666
          item1 {:remote_id "remoteid1" :feature1 -2 :feature2 2 :feature3 15}
          item2 {:remote_id "remoteid2" :feature1 2 :feature2 -1 :feature3 1100}
          features-to-consider '(:feature1 :feature2)]
      (is
        (= expected-value (euclidean-similarity-for-features item1
                                                             item2
                                                             features-to-consider))))))

(deftest tokenize-test
  (testing "Tokenization of genres"
    (let [expected-values #{"reggae" "dancehall" "roots"}]
      (is
        (equal-collections? expected-values (tokenize "roots-reggae reggae dancehall"))))))


(deftest cant-tokenize-empty-test
  (testing "Cant tokenize if both strings are empty"
    (let [item1 {:remote_id "remoteid1" :genres ""}
          item2 {:remote_id "remoteid2" :genres ""}]
      (is
        (true? (cant-tokenize item1
                              item2
                              :genres))))))

(deftest cant-tokenize-not-empty-test
  (testing "Can tokenize if only one string is empty"
    (let [item1 {:remote_id "remoteid1" :genres "reggae dancehall roots-reggae"}
          item2 {:remote_id "remoteid2" :genres ""}]
      (is
        (false? (cant-tokenize item1
                               item2
                               :genres))))))

(deftest similarity-for-tokenizable-feature-cant-tokenize-test
  (testing "zero if both strings are empty"
    (let [item1 {:remote_id "remoteid1" :genres ""}
          item2 {:remote_id "remoteid2" :genres ""}]
      (is
        (= 0 (similarity-for-tokenizable-feature item1
                                                 item2
                                                 :genres))))))

(deftest similarity-for-tokenizable-feature-can-tokenize-test
  (testing "calculates jaccard simmilarity if tokenizable"
    (let [item1 {:remote_id "remoteid1" :genres "reggae dancehall"}
          item2 {:remote_id "remoteid2" :genres "reggae"}]
      (is
        (= 0.5 (similarity-for-tokenizable-feature item1
                                                   item2
                                                   :genres))))))

(deftest overal-similarity-test
  (testing "Calculate both euclidean similarity and genre similarity"
    (let [expected-value 0.6666666666666666
          item1 {:remote_id "remoteid1" :feature1 -2 :feature2 2 :feature3 15 :genres "reggae dancehall"}
          item2 {:remote_id "remoteid2" :feature1 2 :feature2 -1 :feature3 1100 :genres "reggae"}
          features-to-consider '(:feature1 :feature2)]
      (is
        (= expected-value (overal-similarity item1
                                             item2
                                             features-to-consider
                                             :genres))))))

(deftest find-top-similar-test
  (testing "returns three top matches excluding self"
    (let [top-matches '("remoteid2" "remoteid3" "remoteid6")
          item {:remote_id "remoteid1" :feature 10 :genres "rock"}
          data '({:remote_id "remoteid1" :feature 10 :genres "rock"}
                  {:remote_id "remoteid2" :feature 11 :genres "rock"}
                  {:remote_id "remoteid3" :feature 13 :genres "reggae"}
                  {:remote_id "remoteid4" :feature 14 :genres "reggae"}
                  {:remote_id "remoteid5" :feature 15 :genres "reggae"}
                  {:remote_id "remoteid6" :feature 16 :genres "rock"})
          features-to-consider '(:feature)]
      (is
        (equal-collections? top-matches
                            (map :remote_id (find-top-similar item
                                                              data
                                                              features-to-consider
                                                              :genres
                                                              3)))))))

