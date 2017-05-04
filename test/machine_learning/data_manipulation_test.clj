(ns machine_learning.data-manipulation-test
  (:require [clojure.test :refer :all]
            [machine_learning.data-manipulation :refer :all]))

(defn contains-all? [collection1 collection2]
  (every? #(.contains collection1 %) collection2))

(defn equal-collections? [col1 col2]
  (and
    (contains-all? col1 col2)
    (= (count col1) (count col2))))

(deftest min-max-normalization-test
  (testing "Min max normalization"
    (let [expected-value 0.1]
      (is
        (= expected-value (min-max-normalization 0
                                                 100
                                                 10))))))
(deftest normalize-feature-test
  (testing "value normalization for a given feature"
    (let [expected-value 0.2
          min-per-feature {:feature1 10 :feature2 90}
          max-per-feature {:feature1 110 :feature2 100}]
      (is
        (= expected-value (normalize-feature 30
                                             :feature1
                                             min-per-feature
                                             max-per-feature))))))

(deftest normalize-features-test
  (testing "consider only specified features for normalization"
    (let [expected-values '(0.2 0.9)
          item {:feature1 "20" :feature2 "900" :feature3 "1000"}
          min-per-feature {:feature1 0 :feature2 0 :feature3 1000}
          max-per-feature {:feature1 100 :feature2 1000 :feature3 2000}
          result (into {} (normalize-features item
                                              '(:feature1 :feature2)
                                              min-per-feature
                                              max-per-feature))]
      (is
        (= (first expected-values) (:feature1 result)))
      (is
        (= (second expected-values) (:feature2 result))))))

(deftest extract-features-test
  (testing "raw feature extraction - feature value stays the same"
    (let [expected-values '("20" "900")
          item {:feature1 "20" :feature2 "900" :feature3 "1000"}
          result (into {} (extract-features item
                                            '(:feature1 :feature2)))]
      (is
        (= (first expected-values) (:feature1 result)))
      (is
        (= (second expected-values) (:feature2 result))))))

(deftest row->normalized_row-test
  (testing "normalize item - normalizes only features specified for normalization, doesnt change others"
    (let [expected-values '(0.2 0.9 "1000")
          min-per-feature {:feature1 0 :feature2 0 :feature3 1000}
          max-per-feature {:feature1 100 :feature2 1000 :feature3 2000}
          item {:feature1 "20" :feature2 "900" :feature3 "1000"}
          result (row->normalized_row item
                                      '(:feature3)
                                      '(:feature1 :feature2)
                                      min-per-feature
                                      max-per-feature)]
      (is
        (= (first expected-values) (:feature1 result)))
      (is
        (= (second expected-values) (:feature2 result)))
      (is
        (= (nth expected-values 2) (:feature3 result))))))


