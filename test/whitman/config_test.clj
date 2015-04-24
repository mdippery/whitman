(ns whitman.config-test
  (:require [clojure.test :refer :all]
            [whitman.config :as config]))

(deftest test-file-extension
  (is (= (config/file-extension "about.json") "json"))
  (is (= (config/file-extension ".bashrc") ""))
  (is (= (config/file-extension "file") ""))
  (is (= (config/file-extension "a.txt") "txt")))

(deftest test-file-format
  (is (= (config/file-format "about.json") :json))
  (is (= (config/file-format "about.ini") :ini))
  (is (= (config/file-format "about.yaml") :yml))
  (is (= (config/file-format "about.yml") :yml))
  (is (nil? (config/file-format "about.txt"))))

(deftest test-read-reddit-config
  (let [cfg (config/read-config "doc/reddit.json")
        data (get cfg "data")]
    (is (= (get cfg "database") "karmanaut"))
    (is (= (get cfg "collection") "samples"))
    (is (= (get cfg "user-agent") "karmanaut/0.1.0-SNAPSHOT by mipadi - michael@monkey-robot.com"))
    (is (= (get cfg "source") "http://www.reddit.com/user/%s/about.json"))
    (is (= (get cfg "records") "users._id"))
    (is (= (count data) 2))
    (is (= (get (nth data 0) "path") "data.link_karma"))
    (is (= (get (nth data 0) "key") "link_karma"))
    (is (= (get (nth data 1) "path") "data.comment_karma"))
    (is (= (get (nth data 1) "key") "comment_karma"))))

(deftest test-read-stackoverflow-config
  (let [cfg (config/read-config "doc/stackoverflow.json")
        data (get cfg "data")]
    (is (= (get cfg "database") "chameleon"))
    (is (= (get cfg "collection") "samples"))
    (is (= (get cfg "user-agent") "whitman/0.1.0-SNAPSHOT"))
    (is (= (get cfg "source") "http://api.stackexchange.com/2.2/users/%s?site=stackoverflow"))
    (is (= (get cfg "records") "users._id:int"))
    (is (= (count data) 1))
    (is (= (get (nth data 0) "path") "items.0.reputation"))
    (is (= (get (nth data 0) "key") "reputation"))))
