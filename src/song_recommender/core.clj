(ns song-recommender.core
  (:require [clojure-csv.core :as csv]
            [semantic-csv.core :as sc]))

;sc/slurp-csv returns squence of dictionaries
(def data
  (sc/slurp-csv "10KSongs.csv"))

;`remote_id`,track.name,`acousticness`, `danceability`, `energy`, `instrumentalness`, `track_key`, `liveness`, `mode`, `popularity`, `speechiness`, `tempo`, `valence` , `album_year`

(def audio-feature-keys '(
                           :acousticness
                           :danceability
                           :energy
                           :instrumentalness
                           :track_key
                           :liveness
                           :mode
                           :popularity
                           :speechiness
                           :tempo
                           :valence
                           :album_year))


(defn get-nummeric-values [feature, data]
  (map read-string
       (map feature data)))

;reducer works with numbers
(defn get-for-feature [reducer feature-key data]
  (reduce reducer
          (get-nummeric-values feature-key data)))


(defn feature->max-value [keyword]
  [keyword (get-for-feature max keyword data)])

(defn feature->min-value [keyword]
  [keyword (get-for-feature min keyword data)])

;
;((doseq [i (get-feature-values :acousticness data)]
;   (println i (type i))))



(def max_vector (into {} (map feature->max-value audio-feature-keys)))
(def min_vector (into {} (map feature->min-value audio-feature-keys)))


(defn min_max_normalization [minimum maximum value]
  (/ (- value minimum) (- maximum minimum)))

;(print (map #(min_max_normalization 1 5 %) '(1 2 3 4 5)))


(defn normalize [feature data]
  (let [minimum (get-for-feature min feature data)
        maximum (get-for-feature max feature data)]
    (map #(min_max_normalization minimum maximum %)
         (get-nummeric-values feature data))))


(println (first data))
(println (first (normalize :mode data)))