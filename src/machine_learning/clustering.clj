(ns machine_learning.clustering
  (:require [config.global :as global]
            [machine-learning.similarity :as similarity]))


(defn assign-to-means [means items distance features]
  (group-by #(:remote_id (similarity/find-closest-from-items % means distance features))
            items))


(defn average-point [items features]
  (into {}
        (map #(vector % (/ (reduce + (map % items)) (count items)))
             features)))

(defn average-item [items features distance]
  (let [average-point (average-point items features)]
    (similarity/find-closest-from-items average-point items distance features)))

(defn find-new-means [old-means items distance features]
  (map #(average-item % features distance)
       (vals
         (assign-to-means old-means items distance features))))

(defn same-means? [old-means new-means]
  (every? true?
          (map #(.contains (map :remote_id old-means) %)
               (map :remote_id new-means))))

(defn calculate-centroids [initial-means items distance features]
  (loop [old-means initial-means i 0]
    (let [new-means (find-new-means old-means items distance features)]
      (if (same-means? old-means new-means)
        new-means
        (recur new-means (inc i))))))

(defn kmeans [initial-means data distance features]
  (let [clustering-result (assign-to-means (calculate-centroids initial-means data distance features) data distance features)]
    (into {}
          (map #(vector % (map :remote_id (get clustering-result %)))
               (keys clustering-result)))))
