(ns song-recommender.core
  (:require [clojure-csv.core :as csv]
            [semantic-csv.core :as sc]))

;sc/slurp-csv returns squence of dictionaries
(def data
  (sc/slurp-csv "10KSongs.csv"))

(def numeric-features '(:acousticness :danceability :energy :instrumentalness :track_key
                         :liveness :mode :popularity :speechiness :tempo :valence :album_year))

(def non-numeric-features '(:remote_id :genres))

(defn min_max_normalization [minimum maximum value]
  (/ (- value minimum) (- maximum minimum)))


(defn get-nummeric-values [feature, data]
  (map double (map read-string
                   (map feature data))))

(defn get-for-feature [reducer feature-key data]
  (reduce reducer
          (get-nummeric-values feature-key data)))


(def max_per_feature
  (into {}
        (map #(vector % (get-for-feature max % data)) numeric-features)))
(def min_per_feature
  (into {}
        (map #(vector % (get-for-feature min % data)) numeric-features)))

(defn normalize_feature [value feature]
  (let [minimum (feature min_per_feature)
        maximum (feature max_per_feature)]
    (min_max_normalization minimum maximum value)))

;returns: ([:acousticness 0.008524096354463032] [:danceability 0.7777777975429085] [:energy 0.6237306040200037] [:instrumentalness 0.0020820820609149414] [:track_key 1/11] [:liveness 0.34913662657092276] [:mode 1] [:popularity 1] [:speechiness 0.030026811183347855] [:tempo 0.2934798011201785] [:valence 0.35216299666225914] [:album_year 1])
(defn normalize_features [row features]
  (map #(vector % (normalize_feature (read-string (% row)) %)) features))

;returns: ([:remote_id 11hqMWwX7sF3sOGdtijofF] [:genres indie r&b, pop rap])
(defn extract-features [row features]
  (map #(vector % (% row)) features))

;{:valence 0.35216299666225914, :popularity 1, :track_key 1/11, :album_year 1, :genres indie r&b, pop rap, :instrumentalness 0.0020820820609149414, :mode 1, :energy 0.6237306040200037, :speechiness 0.030026811183347855, :liveness 0.34913662657092276, :remote_id 11hqMWwX7sF3sOGdtijofF, :danceability 0.7777777975429085, :tempo 0.2934798011201785, :acousticness 0.008524096354463032}
(defn row->normalized_row [row features_not_to_normalize features_to_normalize]
  (into {} (concat
             (extract-features row features_not_to_normalize) (normalize_features row features_to_normalize))))

;same as data but normalized
(def normalized_data
  (map #(row->normalized_row % non-numeric-features numeric-features) data))

(println (normalize_features (first data) numeric-features))

(println (extract-features (first data) '(:remote_id :genres)))

(println (row->normalized_row (first data) non-numeric-features numeric-features))

(print (first normalized_data))
