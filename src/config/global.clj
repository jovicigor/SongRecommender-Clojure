(ns config.global
  (:require [semantic-csv.core :as sc]))

;sc/slurp-csv returns squence of dictionaries
(defn read-data [path-to-csv]
  (sc/slurp-csv path-to-csv))

(def numeric-features '(:acousticness :danceability :energy :instrumentalness :track_key
                         :liveness :mode :popularity :speechiness :tempo :valence :album_year))

(def non-numeric-features '(:remote_id :genres))

(defn get-nummeric-values [feature, data]
  (map double (map read-string
                   (map feature data))))

(defn get-for-feature [reducer feature-key data]
  (reduce reducer
          (get-nummeric-values feature-key data)))

(defn calculate-per-feature [reducer data]
  (into {} (map #(vector % (get-for-feature reducer % data)) numeric-features)))

(defn euclidean-distance [first-vector second-vector]
  (Math/sqrt (reduce + (map #(Math/pow (- %1 %2) 2) first-vector second-vector))))

(defn euclidean-distance-for-features [map1 map2 features]
  (euclidean-distance (map #(% map1) features)
                      (map #(% map2) features)))

(defn find-closest-from-items [item items distance features]
  (first (sort-by #(distance % item features) items)))

(defn find-closest-different-from-items [item items distance features]
  (let [filtered-items (filter #(not= (:remote_id item) (:remote_id %)) items)]
    (first (sort-by #(distance % item features) filtered-items))))

