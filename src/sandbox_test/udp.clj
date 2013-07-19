(ns sandbox-test.udp
 (import (java.net DatagramPacket DatagramSocket InetAddress))
  (:gen-class))

(def udp-port 9001)

(defn make-socket
  ([] (new DatagramSocket))
  ([port] (new DatagramSocket port)))


(defn receive-data [receive-socket]
  (let [receive-data (byte-array 1024),
        receive-packet (new DatagramPacket receive-data 1024)]
    (.receive receive-socket receive-packet)
    (new String (.getData receive-packet) 0 (.getLength receive-packet))))

(defn make-receive [receive-port]
  (let [receive-socket (make-socket receive-port)]
    (fn [] (receive-data receive-socket))))


;; Open network socket
(def receive-msg (make-receive udp-port) )
