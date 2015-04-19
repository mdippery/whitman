(ns whitman.data-test
  (:require [clojure.test :refer :all]
            [clojure.data.json :as json]
            [whitman.data :as data]
            [whitman.utils :as utils])
  (:import [java.util Date]))

(def default-milliseconds 1429418496343)
(def default-date (Date. default-milliseconds))

(deftest test-record-collection
  (is (= (data/record-collection "users._id") "users")))

(deftest test-int-key-with-string
  (is (not (data/int-key? "users._id"))))

(deftest test-int-key-with-int
  (is (data/int-key? "users._id:int")))

(deftest test-record-key
  (is (= (data/record-key "users._id") "_id")))

(deftest test-record-key-with-int
  (is (= (data/record-key "users._id:int") "_id")))

(deftest test-records-with-string-ids
  (let [cfg {"database" "karmanaut"
             "records" "users._id"}]
    (with-redefs [data/all-records (fn [cfg] [{:_id "mipadi"}])]
      (is (= (data/records cfg) ["mipadi"])))))

(deftest test-records-with-int-ids
  (let [cfg {"database" "chameleon"
             "records" "users._id:int"}]
    (with-redefs [data/all-records (fn [cfg] [{:_id 28804.0}])]
      (is (= (data/records cfg) [28804])))))

(deftest test-path-components
  (is (= (data/path-components "items.0.reputation") ["items", "0", "reputation"])))

(deftest test-get-value-with-vector
  (is (= (data/get-value [{"reputation" 150000}] "0") {"reputation" 150000})))

(deftest test-get-value-with-map
  (is (= (data/get-value {"link_karma" 5000} "link_karma") 5000)))

(deftest test-reduce-data-with-reddit
  (let [data (json/read-str (slurp "fixtures/reddit_mipadi.json"))]
    (is (= (data/reduce-data data "data.link_karma") 4883))
    (is (= (data/reduce-data data "data.comment_karma") 29020))))

(deftest test-reduce-data-with-stackoverflow
  (let [data (json/read-str (slurp "fixtures/stackoverflow_28804.json"))]
    (is (= (data/reduce-data data "items.0.reputation") 158194))))

(deftest test-sample-query
  (with-redefs [utils/utcnow (fn [] default-date)]
    (let [q (data/sample-query 28804)]
      (is (= (:user q) 28804))
      (is (= (:timestamp q) (utils/midnight default-date))))))

(deftest test-sample-insert
  (with-redefs [utils/utcnow (fn [] default-date)]
    (let [doc (data/sample-insert 28804 "reputation" 150000)]
      (is (= (count doc) 1))
      (is (contains? doc "$set"))
      (is (= (get doc "$set") {"reputation.4" 150000})))))
