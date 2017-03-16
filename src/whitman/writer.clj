(ns whitman.writer
  (:require [monger.collection :as mc]
            [whitman.db :as db]))

(defmulti write (fn [writer cfg query docs] writer))
(defmethod write :db [writer cfg query docs]
  (mc/upsert (db/db cfg) (get cfg "collection") query docs {:upsert true}))
(defmethod write :console [writer cfg query docs]
  (do
    (println query)
    (println)
    (println docs)))
