(defproject clj-levy "0.1.0-SNAPSHOT"
  :description "Library for text distance functions as levenshtein."
  :url "https://github.com/TheCodEx/clj-levy"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:dependencies [[junit/junit "4.11"]]}}
  :plugins [[lein-junit "1.1.2"]]          
  :java-source-paths ["src/java" "test/java"]
  :junit ["test/java"]
  :dependencies [[org.clojure/clojure "1.5.1"]])
