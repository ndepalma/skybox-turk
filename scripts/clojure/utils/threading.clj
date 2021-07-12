(ns utils.threading)

(defn on-thread [f]
  "This is a fun function that just puts a function onto a thread, starts it and then moves on."
  (doto (Thread. #^Runnable f)
    (.start)))
