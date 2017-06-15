(defproject digraph "0.1.0"
  :description "Directed graph library written in pure Clojure"
  :url "https://github.com/cjsauer/digraph"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha17"]]
  :monkeypatch-clojure-test false
  :plugins [[lein-auto "0.1.3"]]
  :profiles  {:dev 
              {:dependencies [[org.clojure/test.check "0.9.0"]
                              [org.clojure/tools.namespace "0.2.11"]
                              [orchestra "0.3.0"]]
               :source-paths ["src" "dev"]}})
