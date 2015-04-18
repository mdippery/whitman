(ns whitman.config
  (:require [clojure.data.json :as json]
            [whitman.utils :as utils]))

(def ^{:private true} default-user-agent
  (str "whitman/" utils/version))

(defn ^{:private true } format-config [cfg]
  (let [user-agent (if (contains? cfg "user-agent")
                       (format (get cfg "user-agent") utils/version)
                       default-user-agent)]
    (assoc cfg "user-agent" user-agent)))

(defn read-config [path]
  (-> path
      slurp
      json/read-str
      format-config))
