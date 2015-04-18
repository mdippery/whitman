(ns whitman.utils-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as http]
            [whitman.client :as client]
            [whitman.config :as config]))

(deftest test-reddit-source
  (let [cfg (config/read-config "doc/reddit.json")]
    (is (= (client/source-url cfg "mipadi") "http://www.reddit.com/user/mipadi/about.json"))))

(deftest test-stackoverflow-source
  (let [cfg (config/read-config "doc/stackoverflow.json")]
    (is (= (client/source-url cfg 28804) "http://api.stackexchange.com/2.2/users/28804?site=stackoverflow"))))

(deftest test-request
  (with-redefs [http/get (fn [url params] {:body (slurp "fixtures/reddit_mipadi.json")})]
    (let [cfg (config/read-config "doc/reddit.json")
          resp (client/request cfg "mipadi")
          data (get resp "data")]
      (is (= (get resp "kind") "t2"))
      (is (= (get data "comment_karma") 29020))
      (is (get data "is_mod"))
      (is (nil? (get data "has_verified_email")))
      (is (= (get data "id") "34agu"))
      (is (= (get data "link_karma") 4883))
      (is (= (get data "name") "mipadi"))
      (is (not (get data "is_gold")))
      (is (get data "hide_from_robots"))
      (is (not (get data "is_friend"))))))
