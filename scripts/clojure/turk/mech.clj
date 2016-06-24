(ns turk.mech
  (:import javax.imageio.ImageIO)
  (:import crowd.turkfrastructure.TurkServer)
  (:import crowd.turkfrastructure.HITCreator)
;  (:import com.github.sarxos.webcam.Webcam)
  (:import crowd.turkfrastructure.FindImageQuestion)
  (:import crowd.turkfrastructure.HITjob)
  (:import com.amazonaws.mturk.requester.AssignmentStatus)
  (:import java.awt.image.BufferedImage))

(defonce ^:private sandbox? (atom true))

(defn sandbox
  ([] @sandbox?)
  ([on?]
     (reset! sandbox? on?)))

(defn server []
  (TurkServer/getServer @sandbox?))

(defn image-hit [^BufferedImage img ^String title]
  (crowd.turkfrastructure.HITCreator/createFindImageQuestionFromBI title img @sandbox?))

(defn external-hit [^HITjob hitjob ^String title]
  (crowd.turkfrastructure.HITCreator/createExternalHit title hitjob @sandbox?))


;; (defn snap
;;   ([] (snap "Use the mouse to select a box around and object and label it."))
;;   ([title]
;;      (let [wc (Webcam/getDefault)]
;;         (.open wc)
;;         ;;(ImageIO/write (.getImage wc) "PNG" (clojure.java.io/file "/home/nd/testish.png"))
;;         (image-hit (.getImage wc) title true)
;;         )
;;      ))


(defn get-all-hits []
  (.getAllHits (server)))

(defn get-hit [^String hit-id]
  (.getHITFromID (server) hit-id))

(defn available-assignments? [hit-id]
  (if (= hit-id "0")
    0
    (.getNumberOfAssignmentsAvailable (get-hit hit-id))))

(defn all-submitted? [hit-id]
  (if (= hit-id "0")
    false
    (zero? (available-assignments? hit-id))))

(defn name-from-hit [hit]
  (.getTitle hit))


(defn service []
  (.service (server)))

(defn get-submitted-assignments [hit-id]
    (if (= hit-id "0")
      0
      (let [assng (.getAllAssignmentsForHIT (service)
                                            hit-id
                                            (into-array
                                             com.amazonaws.mturk.requester.AssignmentStatus
                                             [com.amazonaws.mturk.requester.AssignmentStatus/Submitted]))]
        assng)))

(defn submitted-assignments? [hit-id]
  (if (= hit-id "0")
    0
    (count (get-submitted-assignments hit-id))))

(defn expire-hit-id [hit-id]
  (.forceExpireHIT (service) hit-id))


(defn num-hits []
  (.getTotalNumHITsInAccount ( service)))

(defn page-size []
  ;;(com.amazonaws.mturk.service.axis.RequesterService/DEFAULT_PAGE_SIZE)
  10
  )

(defn approve-all [hit-id]
  (let [subd (turk.mech/get-submitted-assignments hit-id)]
    (doseq [assn subd]
      (println "ApprovingAssignment: " assn)
      (.approve (server) assn))))

(defn num-hit-pages []
  (int (+ 0.95 (/ (num-hits)
                  (page-size)))))

(defn get-last-n-hits [n]
  ;;subtract off the last mod page
  ;;add 9 hits to round up
  ;;  divide
  (inc (int (/ (+ 9 ) (page-size))))
  (let [c-n (- n (mod (num-hits) (page-size)))
        n-a (if (= c-n n) 0 1)

        c-p (- c-n (mod c-n (page-size)))
        p-a (if (= c-p c-n) 0 1)

        p (int (/ c-p (page-size)))
        last-n-pages (+ p p-a n-a)

        pages (take-last last-n-pages (range (inc (num-hit-pages))))]
    (take-last n (flatten (map #(seq (.searchHITs (service) %)) pages)))))

(defn hits-to-results [hit]
  ;; get xml, parse xml
  (into [] (FindImageQuestion/parseAnswer (first (seq (.getCurrentResults (server) hit))))))

(defn description
  ([] (HITCreator/getDescription))
  ([^String desc] (HITCreator/setDescription desc)))

(defn reward
  ([] (HITCreator/getReward))
  ([^Double r] (HITCreator/setReward r)))

(defn assignments
  ([] (HITCreator/getNumAssignments))
  ([^Long n] (HITCreator/setNumAssignments n)))

(defn lifetime
  ([] (HITCreator/getLifetimeInSeconds))
  ([^Long t] (HITCreator/setLifetimeInSeconds t)))

(defn duration
  ([] (HITCreator/getAssignmentDurationInSeconds))
  ([^Long t] (HITCreator/setAssignmentDurationInSeconds t)))

(defn approval-delay
  ([] (HITCreator/getAutoApprovalDelayInSeconds))
  ([^Long t] (HITCreator/setAutoApprovalDelayInSeconds t)))

(defn account-balance []
  (.getAccountBalance (service)))

(defn extend-hit-by [n t hit-id]
  (.extendHIT (service) hit-id (int n) t))



;; (defn create-hit
;;   ([^String title ^HITjob hitj]
;;      (create-hit title hitj @sandbox?))
;;   ([^String title ^HITjob hitj ^Boolean sandbox]
;;      (.createExternalHit (service) title hitj sandbox)))
