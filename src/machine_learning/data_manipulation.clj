(ns machine_learning.data-manipulation
  (:require [config.global :as global]))

(defn min-max-normalization [minimum maximum value]
  (/
    (- (double value) minimum)
    (- maximum minimum)))

(defn normalize-feature [value feature min-per-feature max-per-feature]
  (let [minimum (feature min-per-feature)
        maximum (feature max-per-feature)]
    (min-max-normalization minimum
                           maximum
                           value)))

(defn normalize-features [row features min-per-feature max-per-feature]
  (map #(vector % (normalize-feature (read-string (% row)) % min-per-feature max-per-feature))
       features))

(defn extract-features [row features]
  (map #(vector % (% row))
       features))

(defn row->normalized_row [row features-not-to-normalize features-to-normalize min-per-feature max-per-feature]
  (into {}
        (concat
          (extract-features row features-not-to-normalize)
          (normalize-features row features-to-normalize min-per-feature max-per-feature))))
