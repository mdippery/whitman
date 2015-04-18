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

(deftest test-path-components
  (is (= (data/path-components "items.0.reputation") ["items", "0", "reputation"])))

(deftest test-get-value-with-vector
  (is (= (data/get-value [{"reputation" 150000}] "0") {"reputation" 150000})))

(deftest test-get-value-with-map
  (is (= (data/get-value {"link_karma" 5000} "link_karma") 5000)))
