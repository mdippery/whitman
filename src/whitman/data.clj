(ns whitman.data
  (:require [clojure.string :as string]
            [monger.collection :as mc]
            [whitman.db :as db]))

(defn ^{:private true} key-component [key i]
  (nth (string/split key #"\.") i))

(defn record-collection [keypath]
  (key-component keypath 0))

(defn record-key [keypath]
  (key-component keypath 1))

(defn ^{:private true} all-records [cfg]
  (mc/find-maps (db/db cfg) (record-collection (get cfg "records"))))

(defn records [cfg]
  (let [kp (get cfg "records")
        key (keyword (record-key kp))
        recs (all-records cfg)]
    (map #(key %) recs)))
