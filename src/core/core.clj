(ns core.core
  (:require [config.global :as global]
            [machine_learning.data-manipulation :as data]
            [machine_learning.clustering :as clustering]))

(def normalized-data
  (map #(data/row->normalized_row % global/non-numeric-features global/numeric-features) global/raw-data))

(def means (take 100 normalized-data))

(println (map :remote_id (first (clustering/kmeans means normalized-data clustering/euclidean-distance-for-features global/numeric-features))))