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

(defn file-extension [path]
  (let [dot (.lastIndexOf path ".")]
    (if (> dot 0)
        (.substring path (+ dot 1))
        "")))

(defn file-format [path]
  (case (file-extension path)
        "ini"  :ini
        "json" :json
        "yaml" :yml
        "yml"  :yml
        nil))

(defmulti read-config file-format)
(defmethod read-config :json [path]
  (-> path slurp json/read-str format-config))
