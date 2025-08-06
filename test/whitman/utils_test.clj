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

(def version (-> "project.clj" slurp read-string (nth 2)))

(deftest test-version
  (is (= utils/version version)))

(deftest test-utcnow
  (let [now (utils/utcnow)
        tz (TimeZone/getTimeZone "UTC")
        cal (GregorianCalendar. tz)]
    (.setTime cal now)
    ; Assumes this code won't still be in use 10 years from now
    ; 2025-08-06: Ha! Haha! Let's bump this up to 2050, I guess.
    (is (>= (.get cal Calendar/YEAR) 2015))
    (is (< (.get cal Calendar/YEAR) 2050))
    (is (>= (.getTime now) default-milliseconds))
    (is (< (.getTime now) (+ default-milliseconds (* 1000 60 60 24 365 35))))))

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

(deftest test-hours-since-midnight
  (let [delta (utils/hours-since-midnight default-date)]
    (is (= delta 4))))
