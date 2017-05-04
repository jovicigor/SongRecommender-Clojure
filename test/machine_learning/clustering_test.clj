(ns machine_learning.clustering-test
  (:require [clojure.test :refer :all]
            [machine_learning.clustering :refer :all]
            [machine-learning.similarity :refer :all]))

(defn contains-all? [collection1 collection2]
  (every? #(.contains collection1 %) collection2))

(defn equal-collections? [col1 col2]
  (and
    (contains-all? col1 col2)
    (= (count col1) (count col2))))

(deftest find-top-similar-test
  (testing "average point for given features"
    (let [expected-values '(35 55)
          data '({:remote_id "remoteid1" :feature1 10 :feature2 30}
                  {:remote_id "remoteid2" :feature1 20 :feature2 40}
                  {:remote_id "remoteid3" :feature1 30 :feature2 50}
                  {:remote_id "remoteid4" :feature1 40 :feature2 60}
                  {:remote_id "remoteid5" :feature1 50 :feature2 70}
                  {:remote_id "remoteid6" :feature1 60 :feature2 80})
          features-to-consider '(:feature1 :feature2)]
      (is
        (= (first expected-values)
           (:feature1 (average-point data features-to-consider))))
      (is
        (= (second expected-values)
           (:feature2 (average-point data features-to-consider)))))))

(deftest average-item-test
  (testing "average item for given features"
    (let [expected-item-id "remoteid3"
          data '({:remote_id "remoteid1" :feature1 10 :feature2 30}
                  {:remote_id "remoteid2" :feature1 20 :feature2 40}
                  {:remote_id "remoteid3" :feature1 30 :feature2 50}
                  {:remote_id "remoteid4" :feature1 40 :feature2 60}
                  {:remote_id "remoteid5" :feature1 50 :feature2 70}
                  {:remote_id "remoteid6" :feature1 60 :feature2 80})
          features-to-consider '(:feature1 :feature2)]
      (is
        (= expected-item-id
           (:remote_id (average-item data
                                     features-to-consider
                                     euclidean-distance-for-features)))))))

(deftest find-new-means-test
  (testing "that correct mean ids are returned after calculation"
    (let [expected-means '("remoteid2" "remoteid5")
          old-means '({:remote_id "remoteid1" :feature1 10 :feature2 30}
                       {:remote_id "remoteid6" :feature1 60 :feature2 80})
          data '({:remote_id "remoteid1" :feature1 10 :feature2 30}
                  {:remote_id "remoteid2" :feature1 20 :feature2 40}
                  {:remote_id "remoteid3" :feature1 30 :feature2 50}
                  {:remote_id "remoteid4" :feature1 40 :feature2 60}
                  {:remote_id "remoteid5" :feature1 50 :feature2 70}
                  {:remote_id "remoteid6" :feature1 60 :feature2 80})
          features-to-consider '(:feature1 :feature2)
          result (find-new-means old-means
                                 data
                                 euclidean-distance-for-features
                                 features-to-consider)]
      (is
        (equal-collections? expected-means
                            (map :remote_id result))))))

(deftest same-means?-test
  (testing "that correct mean ids are returned after calculation"
    (let [means '({:remote_id "remoteid1" :feature1 10 :feature2 30}
                   {:remote_id "remoteid6" :feature1 60 :feature2 80})]
      (is
        (same-means? means means)))))

(deftest calculate-centroids-test
  (testing "that correct centroids are found"
    (let [expected-centroids '("remoteid3" "remoteid6")
          initial-means '({:remote_id "remoteid1" :feature1 10 :feature2 30}
                           {:remote_id "remoteid6" :feature1 60 :feature2 80})
          data '({:remote_id "remoteid1" :feature1 10 :feature2 30}
                  {:remote_id "remoteid2" :feature1 20 :feature2 40}
                  {:remote_id "remoteid3" :feature1 30 :feature2 50}
                  {:remote_id "remoteid4" :feature1 40 :feature2 60}
                  {:remote_id "remoteid5" :feature1 50 :feature2 70}
                  {:remote_id "remoteid6" :feature1 100 :feature2 130}
                  {:remote_id "remoteid7" :feature1 20 :feature2 90}
                  {:remote_id "remoteid8" :feature1 80 :feature2 200}
                  {:remote_id "remoteid9" :feature1 90 :feature2 89})
          features-to-consider '(:feature1 :feature2)
          result (calculate-centroids initial-means
                                      data
                                      euclidean-distance-for-features
                                      features-to-consider)]
      (is
        (equal-collections? expected-centroids
                            (map :remote_id result))))))

