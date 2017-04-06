(ns config.global
  (:require [semantic-csv.core :as sc]
            [config.util :as util]))


;sc/slurp-csv returns squence of dictionaries
(defn read-data [path-to-csv]
  (sc/slurp-csv path-to-csv))

(def raw-data
  (read-data (util/get-config-param :path-to-csv)))

(def numeric-features '(:acousticness :danceability :energy :instrumentalness :track_key
                         :liveness :mode :popularity :speechiness :tempo :valence :album_year))

(def non-numeric-features '(:remote_id :genres))


