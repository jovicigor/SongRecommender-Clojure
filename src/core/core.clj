(ns core.core
  (:require [config.global :as global]
            [machine_learning.data-manipulation :as data]
            [machine_learning.clustering :as clustering])
  (:gen-class :name com.example.SongRecommenderCore
              :methods [#^{:static true} [performClustering [int java.lang.String] java.util.Map]]))


(defn get-nummeric-values [feature, data]
  (map double (map read-string
                   (map feature data))))

(defn get-for-feature [reducer feature-key data]
  (reduce reducer
          (get-nummeric-values feature-key data)))

(defn calculate-per-feature [reducer data]
  (into {} (map #(vector % (get-for-feature reducer % data)) global/numeric-features)))

(defn take-random-sample [n collection]
  (map #(let [num %] (rand-nth collection)) (range n)))

;returns map: CentroidId: IdsOfItemsInsideCluster
(defn -performClustering [num-of-clusters path-to-csv]
  (let [data (global/read-data path-to-csv)
        min_per_feature (calculate-per-feature min data)
        max_per_feature (calculate-per-feature max data)
        normalized-data (map #(data/row->normalized_row % global/non-numeric-features global/numeric-features min_per_feature max_per_feature) data)
        means (take-random-sample num-of-clusters normalized-data)]
    (clustering/kmeans means normalized-data clustering/euclidean-distance-for-features global/numeric-features)))

