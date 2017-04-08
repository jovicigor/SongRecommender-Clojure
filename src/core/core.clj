(ns core.core
  (:require [config.global :as global]
            [machine_learning.data-manipulation :as data]
            [machine_learning.clustering :as clustering])
  (:gen-class :name com.example.SongRecommenderCore
              :methods [#^{:static true} [performClustering [int java.lang.String] java.util.List]]))
;
;(def normalized-data
;  (map #(data/row->normalized_row % global/non-numeric-features global/numeric-features) global/raw-data))
;
;(def means (take 5 normalized-data))
;
;(println (keys (clustering/kmeans means normalized-data clustering/euclidean-distance-for-features global/numeric-features)))


(defn -performClustering [num-of-clusters]
  (let [normalized-data (map #(data/row->normalized_row % global/non-numeric-features global/numeric-features) global/raw-data)
        means (take num-of-clusters normalized-data)]
    means
    ))

(-performClustering 4)