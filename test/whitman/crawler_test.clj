(ns whitman.crawler-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [clj-http.client :as http]
            [whitman.config :as config]
            [whitman.crawler :as crawler]
            [whitman.utils :as utils])
  (:import [java.util Date]))

(def default-milliseconds 1429418496343)
(def default-date (Date. default-milliseconds))

(deftest test-record-collection
  (is (= (crawler/record-collection "users._id") "users")))

(deftest test-int-key-with-string
  (is (not (crawler/int-key? "users._id"))))

(deftest test-int-key-with-int
  (is (crawler/int-key? "users._id:int")))

(deftest test-record-key
  (is (= (crawler/record-key "users._id") "_id")))

(deftest test-record-key-with-int
  (is (= (crawler/record-key "users._id:int") "_id")))

(deftest test-records-with-string-ids
  (let [cfg {"database" "karmanaut"
             "records" "users._id"}]
    (with-redefs [crawler/all-records (fn [cfg] [{:_id "mipadi"}])]
      (is (= (crawler/records cfg) ["mipadi"])))))

(deftest test-records-with-int-ids
  (let [cfg {"database" "chameleon"
             "records" "users._id:int"}]
    (with-redefs [crawler/all-records (fn [cfg] [{:_id 28804.0}])]
      (is (= (crawler/records cfg) [28804])))))

(deftest test-path-components
  (is (= (crawler/path-components "items.0.reputation") ["items", "0", "reputation"])))

(deftest test-get-value-with-vector
  (is (= (crawler/get-value [{"reputation" 150000}] "0") {"reputation" 150000})))

(deftest test-get-value-with-map
  (is (= (crawler/get-value {"link_karma" 5000} "link_karma") 5000)))

(deftest test-reduce-data-with-reddit
  (let [data (json/read-str (slurp "fixtures/reddit_mipadi.json"))]
    (is (= (crawler/reduce-data data "data.link_karma") 4883))
    (is (= (crawler/reduce-data data "data.comment_karma") 29020))))

(deftest test-reduce-data-with-stackoverflow
  (let [data (json/read-str (slurp "fixtures/stackoverflow_28804.json"))]
    (is (= (crawler/reduce-data data "items.0.reputation") 158194))))

(deftest test-sample-query
  (with-redefs [utils/utcnow (fn [] default-date)]
    (let [q (crawler/sample-query 28804)]
      (is (= (:user q) 28804))
      (is (= (:timestamp q) (utils/midnight default-date))))))

(deftest test-sample-insert
  (with-redefs [utils/utcnow (fn [] default-date)]
    (let [doc (crawler/sample-insert 28804 "reputation" 150000)]
      (is (= (count doc) 1))
      (is (contains? doc "$set"))
      (is (= (get doc "$set") {"reputation.4" 150000})))))

(deftest test-sample-reddit
  (with-redefs [http/get (fn [url params] {:body (slurp "fixtures/reddit_mipadi.json")})]
    (let [cfg (config/read-config "doc/reddit.json")
          point (nth (get cfg "data") 0)]
      (is (= (crawler/sample-datapoint cfg point "mipadi") 4883)))))

(deftest test-sample-stackoverflow
  (with-redefs [http/get (fn [url params] {:body (slurp "fixtures/stackoverflow_28804.json")})]
    (let [cfg (config/read-config "doc/stackoverflow.json")
          point (nth (get cfg "data") 0)]
      (is (= (crawler/sample-datapoint cfg point 28804) 158194)))))

(deftest test-sample-docs
  (with-redefs [utils/utcnow (fn [] default-date)
                http/get (fn [url params] {:body (slurp "fixtures/reddit_mipadi.json")})]
    (let [cfg (config/read-config "doc/reddit.json")
          point (nth (get cfg "data") 0)
          docs (crawler/sample-docs cfg point "mipadi")]
      (is (= (count docs) 2))
      (is (contains? docs :query))
      (is (contains? docs :insert))
      (is (= (:user (:query docs)) "mipadi"))
      (is (= (:timestamp (:query docs)) (utils/midnight default-date)))
      (is (= (:insert docs) {"$set" {"link_karma.4" 4883}})))))
