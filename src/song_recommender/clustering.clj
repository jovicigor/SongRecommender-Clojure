(ns song-recommender.clustering
  (:require [song-recommender.global :as global]))


(defn euclidean-distance [first-vector second-vector]
  (Math/sqrt (reduce + (map #(Math/pow (- %1 %2) 2) first-vector second-vector))))

(defn euclidean-distance-for-features [map1 map2 features]
  (euclidean-distance (map #(% map1) features)
                      (map #(% map2) features)))

;see if the filter is necessary ??
(defn find-closest-from-items [item items distance features]
  (let [items-items-without-item (filter #(not= (:remote_id %) (:remote_id item)) items)]
    (first (sort-by #(distance % item features) items-items-without-item))))


(defn assign-to-means [means items distance features]
  (group-by #(:remote_id (find-closest-from-items % means distance features)) items))


;this method should be in average-item method
(defn average-point [items features]
  (into {}
        (map
          #(vector % (/ (reduce + (map % items)) (count items))) features)))

;find average item from items for average point
(defn average-item [items features distance]
  (let [average-point (average-point items features)]
    (find-closest-from-items average-point items distance features)))

;here you should map values to average-item
(defn find-new-means [old-means items distance features]
  (println "looking for new means")
  (map #(average-item % features distance) (vals (assign-to-means old-means items distance features))))

(defn find-new-means-if-different [old-means items distance features]
  (let [new-means (find-new-means old-means items distance features)]
    (if (every? true? (map #(.contains (map :remote_id old-means) %) (map :remote_id new-means)))
      old-means
      (find-new-means-if-different old-means items distance features))))


(defn are-means-same [old-means new-means]
  (every? true? (map #(.contains (map :remote_id old-means) %) (map :remote_id new-means))))

(defn calculate-centroids [old-means items distance features max-iterations]
  (let [new-means (find-new-means old-means items distance features)]
    (while (not (are-means-same old-means new-means))
      (do
        (- max-iterations 1)
        (find-new-means old-means items distance features)))))

;start with means -> find new means -> assign check difference -> return what you need
