(defproject skybox "1.0-SNAPSHOT"

  :description "Cloud, data, and machine learning oriented Framework"
  :url "http://www.media.mit.edu/~ndepalma"
  :license {:name "MIT License"
            :url "http://en.wikipedia.org/wiki/MIT_License"}

  ;;  :native-path "src/native"; Where to extract native dependencies.
  ;;:compile-paths ["target/classes"]

  ;;:jar-exclusions [#"(?:^|/).svn/"]
  :plugins [[lein-localrepo "0.5.3"]
            [lein-ring "0.8.6"]
            ]
  ;:jvm-opts ["-XX:+TieredCompilation" "-XX:TieredStopAtLevel=1" ]

  :dependencies [[org.clojure/clojure "1.5.0"]]
  :repositories {"local" ~(str (.toURI (java.io.File. "lib")))}

  :profiles {:skybox-turk {:java-source-paths ["src/crowd"]
                           :source-paths ["scripts/clojure/turk" ]
                           :target-path "target/"
                           ;; Directory in which to place AOT-compiled files. Including %s will
                           ;; splice the :target-path into this value.
                           :compile-path "target/classes"
                           :auto-clean false
                           :aot :all ;; [share.sim.SHARESim]
                           ;;:main [ share.sim.SHARESim]
                           :dependencies [[org.clojure/clojure "1.5.1"]
                                          [org.clojars.ghoseb/mongo-java-driver "1.1"]
                                          [org.clojars.zaxtax/java-aws-mturk "1.6.2"]]
                           ;;:omit-source true
                           }})
