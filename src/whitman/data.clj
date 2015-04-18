(ns whitman.data
  (:require [clojure.string :as string]))

(defn ^{:private true} key-component [key i]
  (nth (string/split key #"\.") i))

(defn record-collection [keypath]
  (key-component keypath 0))

(defn record-key [keypath]
  (key-component keypath 1))
