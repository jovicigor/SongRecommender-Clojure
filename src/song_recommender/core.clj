(ns song-recommender.core
  (:require [song-recommender.global :as global]
            [song-recommender.data-manipulation :as data]
            [song-recommender.clustering :as clustering]))


(def normalized-data
  (map #(data/row->normalized_row % global/non-numeric-features global/numeric-features) global/raw-data))

;
;(println (first normalized-data))
;(println (clustering/euclidean-distance-for-features (first normalized-data) (nth normalized-data 1) global/numeric-features))
;(println (clustering/find-closest-from-items (first normalized-data) normalized-data clustering/euclidean-distance-for-features global/numeric-features))


(def means (take 5 normalized-data))
(def subset (take 20 normalized-data))


;(println (clustering/assign-to-means means subset clustering/euclidean-distance-for-features global/numeric-features))
;(println (clustering/average-point means global/numeric-features))
;(println (clustering/average-item means global/numeric-features clustering/euclidean-distance-for-features))


;(println (map :remote_id means))

;(println (map :remote_id (clustering/find-new-means means subset clustering/euclidean-distance-for-features global/numeric-features)))

(println (clustering/calculate-centroids means subset clustering/euclidean-distance-for-features global/numeric-features))