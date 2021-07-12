(ns turk.externalHIT
  (:import crowd.turkfrastructure.HITjob)
  (:gen-class
   :init init))

(defn -init [s])

;; Ignore this. I was playing around with simplifying the types allowed. 
;; (deftype- ExtHIT [:title :descrip :reward :assignments :dur :keywords :lifetime :delay_app :qualif :url :frame_height]

(defn wrap-extquest [^String url ^Integer frameheight]
  "Wraps the question in the headers and footers of the XML schema to publish to mechanical turk. This is a fully clojure based implementation."
  (proxy [HITjob] []
    (render []
      (java.lang.StringBuffer.
       (str
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        "<ExternalQuestion xmlns=\"http://mechanicalturk.amazonaws.com/AWSMechanicalTurkDataSchemas/"
                                  "2006-07-14/ExternalQuestion.xsd\">\n"
        "<ExternalURL>"
        url
        "</ExternalURL>\n"
        "<FrameHeight>"
        frameheight
        "</FrameHeight>\n"
        "</ExternalQuestion>")))))
