(ns whitman.client
  (:require [clojure.data.json :as json]
            [clj-http.client :as http]))

(defn source-url [cfg user]
  (format (get cfg "source") user))

(defn ^{:private true} full-request [cfg user]
  (let [url (source-url cfg user)
        hdrs {"http.useragent" (get cfg "user-agent")}
        params {:client-params hdrs}]
    (http/get url params)))

(defn request [cfg user]
  (->> user
       (full-request cfg)
       :body
       json/read-str))
