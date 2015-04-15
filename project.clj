(def project-version "0.1.0-SNAPSHOT")

(defproject whitman project-version
  :description "Samples web APIs for fun and profit"
  :url "https://github.com/mdippery/whitman"
  :license {:name "3-Clause BSD"
            :url "http://opensource.org/licenses/BSD-3-Clause"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot whitman.core
  :target-path "target/%s"
  :jar-name "whitman.jar"
  :uberjar-name "whitman-standalone.jar"
  :manifest {"Implementation-Version" ~project-version}
  :profiles {:uberjar {:aot :all}})
