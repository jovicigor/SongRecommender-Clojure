(ns machine_learning.data-manipulation
  (:require [config.global :as global]))

(defn min_max_normalization [minimum maximum value]
  (/
    (- (double value) minimum)
    (- maximum minimum)))

(defn normalize_feature [value feature min_per_feature max_per_feature]
  (let [minimum (feature min_per_feature)
        maximum (feature max_per_feature)]
    (min_max_normalization minimum
                           maximum
                           value)))

(defn normalize_features [row features min_per_feature max_per_feature]
  (map #(vector % (normalize_feature (read-string (% row)) % min_per_feature max_per_feature))
       features))

(defn extract-features [row features]
  (map #(vector % (% row))
       features))

(defn row->normalized_row [row features_not_to_normalize features_to_normalize min_per_feature max_per_feature]
  (into {}
        (concat
          (extract-features row features_not_to_normalize)
          (normalize_features row features_to_normalize min_per_feature max_per_feature))))
