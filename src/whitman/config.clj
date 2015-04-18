(ns whitman.config
  (:require [clojure.data.json :as json]))

(defn read-config [path]
  (-> path
      slurp
      json/read-str))
