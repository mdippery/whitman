(ns whitman.core
  (:gen-class))

(defn exit [code msg]
  (binding [*out* *err*]
    (do (println msg)
        (System/exit code))))

(defn -main [& args]
  (if (< (count args) 1)
      (exit 1 "No configuration file specified")
      (println "Hello, world!")))
