(ns leiningen.servlet
  "For deploying a war file to an application server"
  (:require [leiningen.core.project :as project])
  (:require [leiningen.core.main :as main])
  (:require [clojure.string :as str])
  )


(defn profile-merge [entryPt PORT]
  {:ring
   {
    :handler (symbol (str entryPt "/application"))
    :init (symbol (str entryPt "/init-app"))
    :destroy (symbol (str "servlet.utils.glassfish/shutdown-hook"))
    :port  (Integer/parseInt PORT)
    }
   })

(defn build [project entry port ]
  ;get entry and port
;  (println "Entry point: " entry " at port: " port)
  ;merge profiles, default,servlet,parsedargs
  (let [proj-name (last (str/split (str entry) #"\."))
        servletProf (:servlet (:profiles project))
        toMerge (profile-merge entry port)
        profile (project/merge-profiles project [servletProf toMerge {:name proj-name}])]
    ;call uberwar on it
    (main/apply-task "ring" profile ["uberwar"])
    )
  )

(defn run-script [project entry]
  (let [servletProf (:servlet (:profiles project))
        toMerge {:main entry}
        profile (project/merge-profiles project [servletProf toMerge])]
    ;call ring headless on it
    (main/apply-task "run" profile [])
    )

  )

(defn headless-run [project entry port ]
  ;get entry and port
;  (println "Entry point headless: " entry " at port: " port)
  ;merge profiles, default,servlet,parsedargs
  (let [servletProf (:servlet (:profiles project))
        toMerge (profile-merge entry port)
        profile (project/merge-profiles project [servletProf toMerge])]
    ;call ring headless on it
    (main/apply-task "ring" profile ["server-headless"])
    )
  )

(defn async-servlet [project entry]
   ;get entry and port
;  (println "Entry point headless: " entry " at port: " port)
  ;merge profiles, default,servlet,parsedargs
  (let [servletProf (:servlet (:profiles project))
        toMerge {:main (symbol entry)}
        profile (project/merge-profiles project [servletProf toMerge])]
    ;call run headless on it
    (main/apply-task "run" profile [])
    )
  )

(defn servlet [project & args]
  (println "ARGS: " args)
  (if (not (>= (count args) 2))
    (println "Options: \n"
             "   -h : run headless compojure locally\n"
             "   -k : use headless httpKit hack\n"
             "   -s : use as script\n\n"
             "Usage: \n"
             "   lein servlet <entry point> [<port>] [options]")
    (if-not (= (some #{"-s"} args) nil)
      (do
        (println "Running script: " (first args))
        (run-script project (symbol (first args))))
      (if (= (count args) 2)
        (do
          (println "Building project war...")
          (build project (symbol (first args)) (second args)))
        (if-not (= (some #{"-h"} args) nil)
          (do
            (println "Running headless...")
            ;; Run headless compojure instead
            (headless-run project (symbol (first args)) (second args)))
          (if-not (= (some #{"-k"} args) nil)
            (do
              (println "Running httpkit async...")
              (async-servlet project (symbol (first args))))))))))
