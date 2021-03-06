(ns whitman.db-test
  (:require [clojure.test :refer :all]
            [whitman.db :as db]))

(deftest test-db-url-with-default
  (let [cfg {"database" "test"}]
    (is (= (db/db-url cfg) "mongodb://localhost:27017/test"))))

(deftest test-db-url-when-specified
  (let [cfg {"connection" "monkey-robot.com:27017"
             "database" "test"}]
    (is (= (db/db-url cfg) "mongodb://monkey-robot.com:27017/test"))))
