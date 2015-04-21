(ns whitman.crawler
  (:require [clojure.string :as string]
            [monger.collection :as mc]
            [whitman.client :as client]
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

(defn ^{:private true} sample-query [now user]
  {:user user, :timestamp (utils/midnight now)})

(defn ^{:private true} sample-insert [now user key sample]
  (let [hours (utils/hours-since-midnight now)]
    {(str key "." hours) sample}))

(defn ^{:private true} sample-datapoint [cfg data point user]
  (reduce-data data (get point "path")))

(defn ^{:private true} sample-and-insert [now cfg data user datum]
  (sample-insert now user (get datum "key") (sample-datapoint cfg data datum user)))

(defn sample-docs [cfg user]
  (let [now (utils/utcnow)
        resp (client/request cfg user)
        datapoints (get cfg "data")
        q (sample-query now user)
        data (map #(sample-and-insert now cfg resp user %) datapoints)
        ins (utils/merge-map-list data)]
    {:query q, :insert {"$set" ins}}))
