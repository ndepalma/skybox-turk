(ns leiningen.jython
  "For testing at a jython prompt"
  (:require [leiningen.core.project :as project])
  (:require [leiningen.core.main :as main])
  (:require [clojure.string :as str])
  )


;; (defn profile-merge [entryPt PORT]
;;   {:ring
;;    {
;;     :handler (symbol (str entryPt "/application"))
;;     :init (symbol (str entryPt "/init-app"))
;;     :destroy (symbol (str "servlet.utils.glassfish/shutdown-hook"))
;;     :port  (Integer/parseInt PORT)
;;     }
;;    })

(defn dep-merge []
  {:dependencies [[(symbol "org.python/jython-standalone") "2.5.3"]]})

(defn run-script [project entry]
  (let [toMerge {:main "org.python.util.jython"}
        profile (project/merge-profiles project [(dep-merge) toMerge])]
    ;call ring headless on it
    (println profile)
    (main/apply-task "run" profile [])
    )

  )


(defn jython [project & args]
  (run-script project nil ;;(symbol (first args))
              ))
