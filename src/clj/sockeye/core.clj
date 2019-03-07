(ns sockeye.core
  (:import
    [java.net URI]
    [javax.websocket Session EndpointConfig CloseReason]
    [org.glassfish.tyrus.client ClientManager ClientManager$ReconnectHandler ClientProperties]
    [sockeye SockeyeEndpoint]))

(defn- event-handler
  "Event handler extends SockeyeEndpoint class"
  [{:keys [on-open on-text on-error on-close]
    :or   {on-open (constantly nil)
           on-text (constantly nil)
           on-error (constantly nil)
           on-close (constantly nil)}}]
  (proxy [SockeyeEndpoint] []
    (onOpen ^void [^Session session, ^EndpointConfig config]
      (proxy-super onOpen session config)
      (on-open session))
    (onText ^void [^String msg]
      (on-text msg))
    (onError [^Session _, ^Throwable throwable]
      (on-error throwable))
    (onClose [^Session _, ^CloseReason close-reason]
      (on-close
        (.getCode (.getCloseCode close-reason))
        (.getReasonPhrase close-reason)))))

(defn- reconnect-handler
  ""
  [{:keys [on-disconnect on-connect-failure delay]
    :or {on-disconnect (fn [_ _] false)
         on-connect-failure (fn [_] false)
         delay (fn [] 1)}}]
  (proxy [ClientManager$ReconnectHandler] []
    (onDisconnect ^boolean [^CloseReason close-reason]
      (on-disconnect
        (.getCode (.getCloseCode close-reason))
        (.getReasonPhrase close-reason)))
    (onConnectFailure ^boolean [^Exception exception]
      (on-connect-failure exception))
    (getDelay ^long []
      (delay))))

(defn- client
  ""
  [& {:keys [on-disconnect on-connect-failure delay] :as opts}]
  (let [client (ClientManager/createClient)]
    (if-not (nil? opts)
      (.put (.getProperties client)
            ClientProperties/RECONNECT_HANDLER
            (reconnect-handler opts)))
    client))

(defn connect
  ""
  [url client handler]
  (.connectToServer client handler (URI. url)))

(defn disconnect
  ""
  [conn]
  (.close conn))

(defn send-text
  ""
  [conn msg]
  (.sendText (.getBasicRemote conn) msg))



(comment
  ;;example: connect to localhost

  (def url "ws://localhost:9090")
  (def clt (client :on-disconnect (fn [code reason]
                                    (println "disconnected:" code reason)
                                    (println "reconnecting...")
                                    true)
                   :on-connect-failure (fn [ex]
                                         (println (.getMessage ex))
                                         true)
                   :delay (fn [] 1)))

  (def listener (event-handler {:on-open (fn [sess]
                                           (println "connected")
                                           (send-text sess "hello sockeye"))
                                :on-close (fn [code reason]
                                            (println "connection closed:" code reason))
                                :on-text (fn [msg] (println msg))}))

  (def session (connect url clt listener))
  (send-text session "text message")
  (disconnect session)

  )

(comment
  ;; example: connect to coinbase

  (def url "wss://ws-feed.pro.coinbase.com")
  (def sub-msg "{\"type\":\"subscribe\",\"product_ids\":[\"BTC-USD\"],\"channels\":[\"ticker\"]}")

  (def clt (client :on-disconnect (fn [code reason]
                                    (println "disconnected:" code reason)
                                    (println "reconnecting...")
                                    true)
                   :on-connect-failure (fn [ex]
                                         (println (.getMessage ex))
                                         true)
                   :delay (fn [] 1)))

  (def listener (event-handler {:on-open (fn [sess]
                                          (println "connected")
                                          (send-text sess sub-msg))
                               :on-close (fn [code reason]
                                           (println "connection closed:" code reason))
                               :on-text (fn [msg] (println msg))}))

  (def session (connect url clt listener))
  (disconnect session)
  )
