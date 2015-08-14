(ns whitman.core
  (:require [whitman.config :as config]
            [whitman.crawler :as crawler]
            [whitman.db :as db])
  (:gen-class))

(defn exit [code msg]
  (binding [*out* *err*]
    (do (println msg)
        (System/exit code))))

(defn do-crawl [cfg]
  (let [users (crawler/records cfg)
        docs (map #(crawler/sample-docs cfg %) users)]
    (doseq [d docs] (db/insert cfg (:query d) (:insert d)))))

(defn -main [& args]
  (if (< (count args) 1)
    (exit 1 "No configuration file specified")
    (-> (nth args 0)
        config/read-config
        do-crawl)))
