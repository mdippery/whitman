(ns whitman.config
  (:require [clojure.data.json :as json]
            [whitman.utils :as utils]))

(defn format-config [cfg]
  (assoc cfg "user-agent" (format (get cfg "user-agent") utils/version)))

(defn read-config [path]
  (-> path
      slurp
      json/read-str
      format-config))
