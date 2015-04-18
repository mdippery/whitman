(ns whitman.utils
  (:gen-class)
  (:require [clojure.java.io :as io]))

(def version
  (if (.exists (io/as-file "project.clj"))
    (-> "project.clj" slurp read-string (nth 2))
    (-> (eval 'karmanaut.utils) .getPackage .getImplementationVersion)))
