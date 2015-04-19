(ns whitman.utils-test
  (:require [clojure.test :refer :all]
            [whitman.utils :as utils])
  (:import [java.util Calendar
                      Date
                      GregorianCalendar
                      TimeZone]))

(deftest test-version
  (is (= utils/version "0.1.0-SNAPSHOT")))

(deftest test-midnight
  (let [dt (Date. 1429418496343)
        mn (utils/midnight dt)
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
