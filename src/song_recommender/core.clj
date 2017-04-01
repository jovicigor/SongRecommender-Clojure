(ns song-recommender.core
  (:require [song-recommender.global :as global]
            [song-recommender.data-manipulation :as data]))


(println (data/normalize_features (first global/raw-data) global/numeric-features))

(println (data/extract-features (first global/raw-data) '(:remote_id :genres)))

(println (data/row->normalized_row (first global/raw-data) global/non-numeric-features global/numeric-features))

(def normalized-data
  (map #(data/row->normalized_row % global/non-numeric-features global/numeric-features) global/raw-data))


(print (first normalized-data))
