(ns sandbox-test.storm
  (:import [backtype.storm StormSubmitter LocalCluster])
  (:use [backtype.storm clojure config])
  (:use [sandbox-test.udp :as udp])
  (:gen-class))


(defspout socket-spout ["entry"]
  [conf context collector]

  (spout
    (nextTuple []
      (Thread/sleep 100)
      ;;(let [message (receive-msg)])
      ;;(def message receive-msg)
      ;;(println (message))
      ;;(println (receive-msg))
      (emit-spout! collector [(receive-msg)])
      )
    (ack [id]
      ;; You only need to define this method for reliable spouts
      ;; (such as one that reads off of a queue like Kestrel)
      ;; This is an unreliable spout, so it does nothing here
      )))

(defbolt print-bolt ["message"] [str message]
  [conf context collector]
  (bolt
    (println (message))
    (emit-bolt! message)
    (ack! message)))

(defn mk-topology []
  (topology
    {"1" (spout-spec socket-spout)}
    {"2" (bolt-spec {"1" :shuffle} print-bolt)}))

(defn run-local! []
  (let [cluster (LocalCluster.)]
    (.submitTopology cluster "RiotFreeWay.core" {TOPOLOGY-DEBUG true} (mk-topology))
    (Thread/sleep 10000)
    (.shutdown cluster)
    ))

(defn submit-topology! [name]
  (StormSubmitter/submitTopology
    name
    {TOPOLOGY-DEBUG true
     TOPOLOGY-WORKERS 3}
    (mk-topology)))

(defn -main

  ([]
    (println "in -main")
    (run-local!))
  ([name]
    (submit-topology! name)))


(-main)
