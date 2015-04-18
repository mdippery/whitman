(ns whitman.utils-test
  (:require [clojure.test :refer :all]
            [whitman.client :as client]
            [whitman.config :as config]))

(deftest test-reddit-source
  (let [cfg (config/read-config "doc/reddit.json")
        url (get cfg "source")]
    (is (= (client/format-url url "mipadi") "http://www.reddit.com/user/mipadi/about.json"))))

(deftest test-stackoverflow-source
  (let [cfg (config/read-config "doc/stackoverflow.json")
        url (get cfg "source")]
    (is (= (client/format-url url 28804) "http://api.stackexchange.com/2.2/users/28804?site=stackoverflow"))))
