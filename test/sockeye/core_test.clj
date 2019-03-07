(ns sockeye.core-test
  (:require [clojure.test :refer :all]
            [cheshire.core :as json]
            [org.httpkit.server :as http]
            [org.httpkit.timer :as timer]
            [sockeye.core :as client]))

;; (+ 1 (+ 1 (+ 1 1)))

;; LOCAL SERVER TEST

(defn handler [request]
  (http/with-channel request channel
    (http/on-close channel (fn [status]
                             (println "channel closed: " status)))
    (http/on-receive channel (fn [data]
                               (http/send! channel data)))))
 
(defonce server (atom nil))

(defn start-server []
  (println "starting ws server...")
  (reset! server (http/run-server #'handler {:port 8080})))

(defn stop-server []
  (when-not (nil? @server)
    (@server :timeout 100)
    (reset! server nil))
  (println "ws server stopped."))

(deftest local-test
  (testing "local test"
    (start-server)
    (is (= 0 0))
    (stop-server)))

;; GDAX TEST

(deftest gdax-test
  (def gdax-url "wss://ws-feed.pro.coinbase.com")
  (def gdax-sub (json/generate-string {:type "subscribe",
                                       :product_ids ["BTC-USD"]
                                       :channels ["ticker"]}))

  (testing "gdax test"
    (is (= 0 0))))
