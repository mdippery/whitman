(ns whitman.utils-test
  (:require [clojure.test :refer :all]
            [whitman.utils :as utils]))

(deftest test-version
  (is (= utils/version "0.1.0-SNAPSHOT")))
