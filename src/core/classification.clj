(ns core.classification
  (:require [config.global :as global]
            [machine_learning.data-manipulation :as data]))


(defn find-centroid-for-song [path-to-cenroids min-csv max-csv song]
  (let [centroids (global/read-data path-to-cenroids)
        min_per_feature (global/calculate-per-feature min (global/read-data min-csv))
        max_per_feature (global/calculate-per-feature max (global/read-data max-csv))
        normalized-centroids (map #(data/row->normalized_row % global/non-numeric-features global/numeric-features min_per_feature max_per_feature) centroids)
        normalized-song (data/row->normalized_row song global/non-numeric-features global/numeric-features min_per_feature max_per_feature)]
    (println min_per_feature)
    (println max_per_feature)
    (println song)
    (println normalized-song)
    (global/find-closest-from-items normalized-song normalized-centroids global/euclidean-distance-for-features global/numeric-features)))
