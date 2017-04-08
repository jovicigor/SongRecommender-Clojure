(ns core.core
  (:require [config.global :as global]
            [machine_learning.data-manipulation :as data]
            [machine_learning.clustering :as clustering])
  (:gen-class :name com.example.SongRecommenderCore
              :methods [#^{:static true} [performClustering [int] java.util.Map]]))


;returns map: CentroidId: IdsOfItemsInsideCluster
(defn -performClustering [num-of-clusters]
  (let [normalized-data (map #(data/row->normalized_row % global/non-numeric-features global/numeric-features) global/raw-data)
        means (take num-of-clusters normalized-data)]
    (clustering/kmeans means normalized-data clustering/euclidean-distance-for-features global/numeric-features)))

;(println (first (-performClustering 4)))