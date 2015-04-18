(ns whitman.config-test
  (:require [clojure.test :refer :all]
            [whitman.config :as config]))

(deftest test-read-config
  (let [cfg (config/read-config "doc/reddit.json")
        data (get cfg "data")]
    (is (= (get cfg "database") "karmanaut"))
    (is (= (get cfg "collection") "samples"))
    (is (= (get cfg "user-agent") "karmanaut/%s by mipadi - michael@monkey-robot.com"))
    (is (= (get cfg "source") "http://www.reddit.com/user/%s/about.json"))
    (is (= (get cfg "records") "users._id"))
    (is (= (count data) 2))
    (is (= (get (nth data 0) "path") "data.link_karma"))
    (is (= (get (nth data 0) "key") "link_karma"))
    (is (= (get (nth data 1) "path") "data.comment_karma"))
    (is (= (get (nth data 1) "key") "comment_karma"))))
