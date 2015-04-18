(ns whitman.db-test
  (:require [clojure.test :refer :all]
            [whitman.db :as db]))

(deftest test-db-url-with-default
  (let [cfg {"database" "test"}]
    (is (= (db/db-url cfg) "mongodb://localhost:27017/test"))))

(deftest test-db-url-when-specified
  (let [cfg {"connection" "whitman.monkey-robot.com:27017"
             "database" "test"}]
    (is (= (db/db-url cfg) "mongodb://whitman.monkey-robot.com:27017/test"))))

(deftest test-db-with-default
  (let [cfg {"database" "test"}]
    (-> cfg
        db/db
        nil?
        not
        is)))

(deftest test-db-when-specified
  (let [cfg {"connection" "whitman.monkey-robot.com:27017"
             "database" "test"}]
    (-> cfg
        db/db
        nil?
        not
        is)))
