(ns whitman.data-test
  (:require [clojure.test :refer :all]
            [whitman.data :as data]))

(deftest test-record-collection
  (is (= (data/record-collection "users._id") "users")))

(deftest test-record-key
  (is (= (data/record-key "users._id") "_id")))

(deftest test-records
  (let [cfg {"database" "karmanaut"
             "records" "users._id"}]
    (with-redefs [data/all-records (fn [cfg] [{:_id "mipadi"}])]
      (is (= (data/records cfg) ["mipadi"])))))
