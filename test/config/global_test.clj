(ns config.global-test
  (:require [clojure.test :refer :all]
            [config.global :refer :all]))

(defn contains-all? [collection1 collection2]
  (every? #(.contains collection1 %) collection2))

(defn equal-collections? [col1 col2]
  (and
    (contains-all? col1 col2)
    (= (count col1) (count col2))))

(deftest numeric-features-test
  (testing "All numeric features considered."
    (let [expected-features '(:acousticness :danceability :energy :instrumentalness :track_key
                               :liveness :mode :popularity :speechiness :tempo :valence :album_year)]
      (is
        (equal-collections? expected-features
                            numeric-features)))))

(deftest non-numeric-features-test
  (testing "All non numeric features considered."
    (let [expected-features '(:remote_id :genres)]
      (is
        (equal-collections? expected-features
                            non-numeric-features)))))

(deftest get-nummeric-values-test
  (testing "All numeric values returned"
    (let [expected-values '(10.0 11.0 12.0)
          data '({:remote_id "remoteid1" :feature "10" :feature2 "15"}
                  {:remote_id "remoteid2" :feature "11" :feature2 "16"}
                  {:remote_id "remoteid3" :feature "12" :feature2 "17"})
          result (get-nummeric-values :feature data)]
      (is
        (equal-collections? expected-values result)))))

(deftest get-for-feature-max-test
  (testing "Maximum reduction for feature"
    (let [expected-value 12.0
          data '({:remote_id "remoteid1" :feature "10" :feature2 "15"}
                  {:remote_id "remoteid2" :feature "11" :feature2 "16"}
                  {:remote_id "remoteid3" :feature "12" :feature2 "17"})
          result (get-for-feature max :feature data)]
      (is
        (= expected-value result)))))

(deftest get-for-feature-min-test
  (testing "Minimum reduction for feature"
    (let [expected-value 10.0
          data '({:remote_id "remoteid1" :feature "10" :feature2 "15"}
                  {:remote_id "remoteid2" :feature "11" :feature2 "16"}
                  {:remote_id "remoteid3" :feature "12" :feature2 "17"})
          result (get-for-feature min :feature data)]
      (is
        (= expected-value result)))))

(deftest calculate-per-feature-min-test
  (testing "Per feature minimums."
    (let [expected-values '(10.0 15.0)
          data '({:remote_id "remoteid1" :feature "10" :feature2 "15"}
                  {:remote_id "remoteid2" :feature "11" :feature2 "16"}
                  {:remote_id "remoteid3" :feature "12" :feature2 "17"})
          result (calculate-per-feature min data '(:feature :feature2))]
      (is
        (and (= (first expected-values) (:feature result))
             (= (second expected-values) (:feature2 result)))))))

(deftest calculate-per-feature-max-test
  (testing "Per feature maximums"
    (let [expected-values '(12.0 17.0)
          data '({:remote_id "remoteid1" :feature "10" :feature2 "15"}
                  {:remote_id "remoteid2" :feature "11" :feature2 "16"}
                  {:remote_id "remoteid3" :feature "12" :feature2 "17"})
          result (calculate-per-feature max data '(:feature :feature2))]
      (is
        (and (= (first expected-values) (:feature result))
             (= (second expected-values) (:feature2 result)))))))

