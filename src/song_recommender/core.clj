(ns song-recommender.core
  (:require [song-recommender.global :as global]
            [song-recommender.data-manipulation :as data]))


(def normalized-data
  (map #(data/row->normalized_row % global/non-numeric-features global/numeric-features) global/raw-data))


(print (first normalized-data))
