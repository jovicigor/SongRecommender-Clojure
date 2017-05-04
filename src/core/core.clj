(ns core.core
  (:require [config.global :as global]
            [machine_learning.data-manipulation :as data]
            [machine_learning.clustering :as clustering]
            [machine-learning.similarity :as similarity])
  (:gen-class :name com.example.SongRecommenderCore
              :methods [[performClustering [int java.lang.String] java.util.Map],
                        [performClusteringWithCentroidsWithGenres [int java.lang.String java.lang.String] java.util.Map]]))



(defn take-random-sample [n collection]
  (map #(let [num %] (rand-nth collection)) (range n)))

(defn take-means [means-ids data]
  (filter #(.contains means-ids (:remote_id %)) data))

;returns map: CentroidId: IdsOfItemsInsideCluster
(defn -performClustering [this num-of-clusters path-to-csv]
  (let [data (global/read-data path-to-csv)
        min_per_feature (global/calculate-per-feature min
                                                      data
                                                      global/numeric-features)
        max_per_feature (global/calculate-per-feature max
                                                      data
                                                      global/numeric-features)
        normalized-data (map
                          #(data/row->normalized_row %
                                                     global/non-numeric-features
                                                     global/numeric-features min_per_feature
                                                     max_per_feature)
                          data)
        means (take-random-sample num-of-clusters
                                  normalized-data)]
    (clustering/kmeans means
                       normalized-data
                       similarity/euclidean-distance-for-features
                       global/numeric-features)))

(defn -performClusteringWithCentroidsWithGenres [this num-of-clusters path-to-csv path-to-centroid-candidates]
  (let [data (global/read-data path-to-csv)
        min_per_feature (global/calculate-per-feature min
                                                      data
                                                      global/numeric-features)
        max_per_feature (global/calculate-per-feature max
                                                      data
                                                      global/numeric-features)
        normalized-data (map
                          #(data/row->normalized_row %
                                                     global/non-numeric-features
                                                     global/numeric-features
                                                     min_per_feature
                                                     max_per_feature)
                          data)
        means (take-means (map :remote_id
                               (take-random-sample num-of-clusters
                                                   (global/read-data path-to-centroid-candidates)))
                          normalized-data)]
    (clustering/kmeans means
                       normalized-data
                       similarity/euclidean-distance-for-features
                       global/numeric-features)))
