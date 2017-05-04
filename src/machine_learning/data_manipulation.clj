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

;returns: ([:acousticness 0.008524096354463032] [:danceability 0.7777777975429085] [:energy 0.6237306040200037] [:instrumentalness 0.0020820820609149414] [:track_key 1/11] [:liveness 0.34913662657092276] [:mode 1] [:popularity 1] [:speechiness 0.030026811183347855] [:tempo 0.2934798011201785] [:valence 0.35216299666225914] [:album_year 1])
(defn normalize_features [row features min_per_feature max_per_feature]
  (map #(vector % (normalize_feature (read-string (% row)) % min_per_feature max_per_feature))
       features))

;returns: ([:remote_id 11hqMWwX7sF3sOGdtijofF] [:genres indie r&b, pop rap])
(defn extract-features [row features]
  (map #(vector % (% row))
       features))

;{:valence 0.35216299666225914, :popularity 1, :track_key 1/11, :album_year 1, :genres indie r&b, pop rap, :instrumentalness 0.0020820820609149414, :mode 1, :energy 0.6237306040200037, :speechiness 0.030026811183347855, :liveness 0.34913662657092276, :remote_id 11hqMWwX7sF3sOGdtijofF, :danceability 0.7777777975429085, :tempo 0.2934798011201785, :acousticness 0.008524096354463032}
(defn row->normalized_row [row features_not_to_normalize features_to_normalize min_per_feature max_per_feature]
  (into {}
        (concat
          (extract-features row features_not_to_normalize)
          (normalize_features row features_to_normalize min_per_feature max_per_feature))))
