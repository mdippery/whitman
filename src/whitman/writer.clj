(ns whitman.writer
  (:require [monger.collection :as mc]
            [whitman.db :as db]))

(defn write [cfg query docs]
  (mc/upsert (db/db cfg) (get cfg "collection") query docs {:upsert true}))
