(ns whitman.crawler
  (:require [clojure.string :as string]
            [monger.collection :as mc]
            [whitman.db :as db]
            [whitman.utils :as utils]))

(defn ^{:private true} key-component [key i]
  (nth (string/split key #"\.") i))

(defn record-collection [keypath]
  (key-component keypath 0))

(defn record-key [keypath]
  (let [k (key-component keypath 1)]
    (nth (string/split k #":") 0)))

(defn int-key? [keypath]
  (> (count (string/split keypath #":")) 1))

(defn ^{:private true} all-records [cfg]
  (mc/find-maps (db/db cfg) (record-collection (get cfg "records"))))

(defn records [cfg]
  (let [kp (get cfg "records")
        key (keyword (record-key kp))
        recs (all-records cfg)
        ids (map #(key %) recs)]
    (if (int-key? kp)
        (map int ids)
        ids)))

(defn path-components [keypath]
  (string/split keypath #"\."))

(defmulti get-value (fn [obj k] (type obj)))
(defmethod get-value clojure.lang.PersistentVector [obj k]
  (nth obj (Integer. k)))
(defmethod get-value clojure.lang.PersistentArrayMap [obj k]
  (get obj k))
(defmethod get-value clojure.lang.PersistentHashMap [obj k]
  (get obj k))

(defn reduce-data [data path]
  (reduce get-value data (path-components path)))

(defn sample-query [user]
  {:user user, :timestamp (utils/midnight (utils/utcnow))})

(defn sample-insert [user key sample]
  (let [hours (utils/hours-since-midnight (utils/utcnow))]
    {"$set" {(str key "." hours) sample}}))
