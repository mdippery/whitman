(ns whitman.utils
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:import [java.util Calendar
                      Date
                      GregorianCalendar
                      TimeZone]))

(def version
  (if (.exists (io/as-file "project.clj"))
    (-> "project.clj" slurp read-string (nth 2))
    (-> (eval 'karmanaut.utils) .getPackage .getImplementationVersion)))

(defn utcnow []
  (Date.))

(defn midnight [dt]
  (let [tz (TimeZone/getTimeZone "UTC")
        c (GregorianCalendar. tz)]
    (doto c
      (.setTime dt)
      (.set Calendar/HOUR_OF_DAY 0)
      (.set Calendar/MINUTE 0)
      (.set Calendar/SECOND 0)
      (.set Calendar/MILLISECOND 0))
    (.getTime c)))

(defn seconds-since-epoch [dt]
  (quot (.getTime dt) 1000))

(defn hours-since-midnight [dt]
  (let [mn (midnight dt)
        mnsec (seconds-since-epoch mn)
        dtsec (seconds-since-epoch dt)]
    (quot (- dtsec mnsec) (* 60 60))))
