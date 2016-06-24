(ns utils.threading)

(defn on-thread [f]
  (doto (Thread. #^Runnable f) 
    (.start)))