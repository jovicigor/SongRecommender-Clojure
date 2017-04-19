(ns core.classification
  (:require [config.global :as global]
            [machine_learning.data-manipulation :as data])
  (:gen-class :name com.songrecommender.Classifier
              :methods [[findClosestCentroids [java.lang.String java.lang.String java.lang.String java.util.Map] java.lang.String]
                        [findSimilarWithEuclideanDistance [java.lang.String java.lang.String java.lang.String java.util.Map] java.lang.String]]))


(defn -findClosestCentroids [this path-to-cenroids path-to-min-csv path-to-max-csv song]
  (let [centroids (global/read-data path-to-cenroids)
        min_per_feature (global/calculate-per-feature min (global/read-data path-to-min-csv))
        max_per_feature (global/calculate-per-feature max (global/read-data path-to-max-csv))
        normalized-centroids (map #(data/row->normalized_row % global/non-numeric-features global/numeric-features min_per_feature max_per_feature) centroids)
        normalized-song (data/row->normalized_row song global/non-numeric-features global/numeric-features min_per_feature max_per_feature)]
    (:remote_id (global/find-closest-from-items normalized-song normalized-centroids global/euclidean-distance-for-features global/numeric-features))))

(defn -findSimilarWithEuclideanDistance [this path-to-cluster path-to-min-csv path-to-max-csv song]
  (let [cluster-items (global/read-data path-to-cluster)
        min_per_feature (global/calculate-per-feature min (global/read-data path-to-min-csv))
        max_per_feature (global/calculate-per-feature max (global/read-data path-to-max-csv))
        normalized-cluster (map #(data/row->normalized_row % global/non-numeric-features global/numeric-features min_per_feature max_per_feature) cluster-items)
        normalized-song (data/row->normalized_row song global/non-numeric-features global/numeric-features min_per_feature max_per_feature)]
    (:remote_id (global/find-closest-different-from-items normalized-song normalized-cluster global/euclidean-distance-for-features global/numeric-features))))
