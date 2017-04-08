(ns config.global
  (:require [semantic-csv.core :as sc]))


;sc/slurp-csv returns squence of dictionaries
(defn read-data [path-to-csv]
  (sc/slurp-csv path-to-csv))

(def raw-data
  (read-data "src/10KSongs.csv"))

(def numeric-features '(:acousticness :danceability :energy :instrumentalness :track_key
                         :liveness :mode :popularity :speechiness :tempo :valence :album_year))

(def non-numeric-features '(:remote_id :genres))


