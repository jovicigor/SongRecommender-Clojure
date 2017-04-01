(ns song-recommender.util
  (:require [clojure.edn :as edn]))


(defn get-config-param [param-key]
  (param-key (edn/read-string (slurp "config.edn"))))

