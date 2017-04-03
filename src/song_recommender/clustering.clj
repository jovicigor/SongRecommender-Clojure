(ns song-recommender.clustering
  (:require [song-recommender.global :as global]))


(defn euclidean-distance [first-vector second-vector]
  (Math/sqrt (reduce + (map #(Math/pow (- %1 %2) 2) first-vector second-vector))))

(defn euclidean-distance-for-features [map1 map2 features]
  (euclidean-distance (map #(% map1) features)
                      (map #(% map2) features)))

(defn find-closest-from-items [item items distance features]
  (let [items-items-without-item (filter #(not= (:remote_id %) (:remote_id item)) items)]
    (first (sort-by #(distance % item features) items-items-without-item))))


(defn assign-to-means [means items distance features]
  (group-by #(:remote_id (find-closest-from-items % means distance features)) items))

(defn average-point [items features]
  (into {}
        (map
          #(vector % (/ (reduce + (map % items)) (count items))) features)))

;find average item from items for average point
(defn average-item [items features distance]
  (let [average-point (average-point items features)]
    (find-closest-from-items average-point items distance features)))


