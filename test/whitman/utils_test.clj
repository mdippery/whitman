(ns whitman.utils-test
  (:require [clojure.test :refer :all]
            [whitman.utils :as utils])
  (:import [java.util Calendar
                      Date
                      GregorianCalendar
                      TimeZone]))

(def default-milliseconds 1429418496343)
(def default-seconds (quot default-milliseconds 1000))
(def default-date (Date. default-milliseconds))

(deftest test-version
  (is (= utils/version "0.1.0-SNAPSHOT")))

(deftest test-midnight
  (let [mn (utils/midnight default-date)
        tz (TimeZone/getTimeZone "UTC")
        cal (GregorianCalendar. tz)]
    (.setTime cal mn)
    (is (= (.get cal Calendar/YEAR) 2015))
    (is (= (.get cal Calendar/MONTH) 3))
    (is (= (.get cal Calendar/DAY_OF_MONTH) 19))
    (is (= (.get cal Calendar/HOUR) 0))
    (is (= (.get cal Calendar/MINUTE) 0))
    (is (= (.get cal Calendar/SECOND) 0))
    (is (= (.get cal Calendar/MILLISECOND) 0))))

(deftest test-seconds-since-epoch
  (let [delta (utils/seconds-since-epoch default-date)]
    (is (= delta default-seconds))))
