(def project-version "0.1.0-SNAPSHOT")

(defproject karmabot project-version
  :description "Scrapes web APIs for fun and profit"
  :url "https://github.com/mdippery/karmabot"
  :license {:name "3-Clause BSD"
            :url "http://opensource.org/licenses/BSD-3-Clause"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :main ^:skip-aot karmabot.core
  :target-path "target/%s"
  :jar-name "karmabot.jar"
  :uberjar-name "karmabot-standalone.jar"
  :manifest {"Implementation-Version" ~project-version}
  :profiles {:uberjar {:aot :all}})
