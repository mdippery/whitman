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

(defn path-components [keypath]
  (string/split keypath #"\."))

(defmulti get-value (fn [obj k] (type obj)))
(defmethod get-value clojure.lang.PersistentVector [obj k]
  (nth obj (Integer. k)))
(defmethod get-value clojure.lang.PersistentArrayMap [obj k]
  (get obj k))
(defmethod get-value clojure.lang.PersistentHashMap [obj k]
  (get obj k))
