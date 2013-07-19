(ns sandbox-test.core
;  (require [sandbox-test.udp :as udp])
;  (:import [server-socket])
  (:import (java.net DatagramSocket DatagramPacket ))
  (:gen-class))


(def socket (DatagramSocket. 9001))

(def running(atom true))
(def buffer(make-array Byte/TYPE 1024))
(defn parse [packet]
;;  (println (String. (.getData packet) 0 (.getLength packet))))
  (println (String. (.getData packet))))

(defn start-receiver []
  (while (true? @running)
    (let [packet (DatagramPacket. buffer 1024)]
      (do
        (.receive socket packet)
        (future (parse packet)))
      )
    )
  )

(defn -main
  [& args]
  ;; work around dangerous default behaviour in Clojure
  (alter-var-root #'*read-eval* (constantly false))
  ;;(println "Hello, World!"))
  (println "foobar")
  (start-receiver))
