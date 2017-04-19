(ns config.global
  (:require [semantic-csv.core :as sc]
            [clojure.string :as str]
            [clojure.set :as sets]))

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