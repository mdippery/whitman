(ns whitman.db
  (:require [monger.collection :as mc]
            [monger.core :as mg]))

(def ^{:private true} default-db-host "localhost:27017")

(defn db-url [cfg]
  (let [host (get cfg "connection" default-db-host)
        db (get cfg "database")]
    (str "mongodb://" host "/" db)))

(defn db [cfg]
  (-> cfg db-url mg/connect-via-uri :db))

(defn insert [cfg query docs]
  (mc/upsert (db cfg) (get cfg "collection") query docs {:upsert true}))
