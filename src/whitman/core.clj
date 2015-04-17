(ns whitman.core
  (:require [clojure.data.json :as json])
  (:gen-class))

(defn exit [code msg]
  (binding [*out* *err*]
    (do (println msg)
        (System/exit code))))

(defn -main [& args]
  (if (< (count args) 1)
      (exit 1 "No configuration file specified")
      (-> (nth args 0)
          slurp
          json/read-str
          println)))
