(ns turk.mech
  (:import javax.imageio.ImageIO)
  (:import crowd.turkfrastructure.TurkServer)
  (:import crowd.turkfrastructure.HITCreator)
;  (:import com.github.sarxos.webcam.Webcam)
  (:import crowd.turkfrastructure.FindImageQuestion)
  (:import crowd.turkfrastructure.HITjob)
  (:import com.amazonaws.mturk.requester.AssignmentStatus)
  (:import java.awt.image.BufferedImage))

;; This is global that tells us whether we are on the sandbox or not.
(defonce ^:private sandbox? (atom true))


;;This is a fun function that just puts a function onto a thread, starts it and then oves on.
(defn sandbox
  ([] @sandbox?)
  ([on?]
     (reset! sandbox? on?)))

(defn server []
  "Use the global sandbox boolean and create a connection to mechanical turk"
  (TurkServer/getServer @sandbox?))

(defn image-hit [^BufferedImage img ^String title]
  "Create an image hit using the buffered image"
  (crowd.turkfrastructure.HITCreator/createFindImageQuestionFromBI title img @sandbox?))

(defn external-hit [^HITjob hitjob ^String title]
  "Deploy a HIT to the server"
  (crowd.turkfrastructure.HITCreator/createExternalHit title hitjob @sandbox?))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This is a function if you have the sarxos apis installed. Some people have reported that being a challenge to install.
;; So I'm leaving this out of the build for that reason.
;;
;; However, if you do have that installed, then this function will Open the webcam, take a picture, and create a hit with it.
;; Kind of cool! Be careful what you point the camera at! 
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
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
  "Get all of the hits from the amazon server"
  (.getAllHits (server)))

(defn get-hit [^String hit-id]
  "Get a specific HIT if you have the HIT id"
  (.getHITFromID (server) hit-id))

(defn available-assignments? [hit-id]
  "Check how many available assignments are left for a particular hit-id"
  (if (= hit-id "0")
    0
    (.getNumberOfAssignmentsAvailable (get-hit hit-id))))

(defn all-submitted? [hit-id]
  "Check to see if all of the HITs are submitted and it's done and ready to be approved"
  (if (= hit-id "0")
    false
    (zero? (available-assignments? hit-id))))

(defn name-from-hit [hit]
  "Gets the title from the hit"
  (.getTitle hit))


(defn service []
  "Accessor for the service of the server so that you can pass it on to other function calls."
  (.service (server)))

(defn get-submitted-assignments [hit-id]
  "Get the data of the submitted assignments for a particular hit-id. This can take a while."
    (if (= hit-id "0")
      0
      (let [assng (.getAllAssignmentsForHIT (service)
                                            hit-id
                                            (into-array
                                             com.amazonaws.mturk.requester.AssignmentStatus
                                             [com.amazonaws.mturk.requester.AssignmentStatus/Submitted]))]
        assng)))

(defn submitted-assignments? [hit-id]
  "This just gets the number of submitted assignments with results for a hit-id."
  (if (= hit-id "0")
    0
    (count (get-submitted-assignments hit-id))))

(defn expire-hit-id [hit-id]
  "Expire a HIT early given a hit-id."
  (.forceExpireHIT (service) hit-id))


(defn num-hits []
  "Check in on the number of HITS currently running"
  (.getTotalNumHITsInAccount ( service)))

(defn page-size []
  "What's the desired page size?"
  ;;(com.amazonaws.mturk.service.axis.RequesterService/DEFAULT_PAGE_SIZE)
  10
  )

(defn approve-all [hit-id]
  "Approve all of the assignments for a given HIT-id. "
  (let [subd (turk.mech/get-submitted-assignments hit-id)]
    (doseq [assn subd]
      (println "ApprovingAssignment: " assn)
      (.approve (server) assn))))

(defn num-hit-pages []
  "Part of the experiment - take the number of hits and divide by page size (on the requester.mturk.com)"
  (int (+ 0.95 (/ (num-hits)
                  (page-size)))))

(defn get-last-n-hits [n]
  "Part of the experiment - gets the last HITs on the pages. You can use this if you are generating a ton of HITs"
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
  "get xml, parse xml for a find image question."
  (into [] (FindImageQuestion/parseAnswer (first (seq (.getCurrentResults (server) hit))))))

(defn description
  ([] (HITCreator/getDescription)) ;; Get the description
  ([^String desc] (HITCreator/setDescription desc))) ;; Set the description

(defn reward
  ([] (HITCreator/getReward)) ;; Get the current reward 
  ([^Double r] (HITCreator/setReward r))) ;; set the current reward

(defn assignments
  ([] (HITCreator/getNumAssignments)) ;; Get the number of assignments for the hit
  ([^Long n] (HITCreator/setNumAssignments n))) ;; Set the number of assignments for the hit

(defn lifetime
  ([] (HITCreator/getLifetimeInSeconds)) ;; Get the total lifetime of the hit before it expires
  ([^Long t] (HITCreator/setLifetimeInSeconds t))) ;; Set the total lifetime of the hit before it expires.

(defn duration
  ([] (HITCreator/getAssignmentDurationInSeconds)) ;; Get the time alloted for the worker
  ([^Long t] (HITCreator/setAssignmentDurationInSeconds t))) ;; Set the time alloted for the worker

(defn approval-delay
  ([] (HITCreator/getAutoApprovalDelayInSeconds)) ;; Get the time alloted to me to review the assignment
  ([^Long t] (HITCreator/setAutoApprovalDelayInSeconds t))) ;; set the time alloted to me to review the assignment

(defn account-balance []
  "See how much money I have left"
  (.getAccountBalance (service)))

(defn extend-hit-by [n t hit-id]
  "Extend a hit by more seconds. This is useful if you are dynamically modifying your workload."
  (.extendHIT (service) hit-id (int n) t))

