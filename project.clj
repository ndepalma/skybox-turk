(defproject skybox-turk "1.0-SNAPSHOT"

  :description "Cloud, data, and machine learning oriented Framework"
  :url "http://www.media.mit.edu/~ndepalma"
  :license {:name "MIT License"
            :url "http://en.wikipedia.org/wiki/MIT_License"}

  :jar-exclusions [#"(?:^|/).git/"]
  :plugins [[lein-localrepo "0.5.3"]
            [lein-ring "0.8.6"]]

  ;; :dev-dependencies [
  ;;           [org.clojure/clojure "1.5.1"]]
  :dependencies [
                 ;; [org.clojure/clojure "1.5.1"]
                 ;; [org.clojars.ghoseb/mongo-java-driver "1.1"]
                 [org.clojars.zaxtax/java-aws-mturk "1.6.2"]
                 [log4j "1.2.17"]
                 ;; [org.python/jython-standalone "2.5.3"]
                 ]

  :repositories {"local" ~(str (.toURI (java.io.File. "lib")))}
  ;; :main org.python.util.jython
  :java-source-paths ["src/crowd"
                      "src/utils"]
  ;; :source-paths ["scripts/clojure/" ]
  ;; :target-path "target/"
  ;; Directory in which to place AOT-compiled files. Including %s will
  ;; splice the :target-path into this value.
  :compile-path "target/classes"
  ;; :auto-clean true
  ;; :aot :all
  ;; :omit-source true

  )
