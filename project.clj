(defproject ogimart/sockeye "0.1.0-alpha"
  :description "A Websocket Client for Clojure"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [cheshire "5.8.1"]
                 [com.stuartsierra/component "0.4.0"]
                 [javax.websocket/javax.websocket-api "1.1"]
                 [org.glassfish.tyrus.bundles/tyrus-standalone-client "1.15"]]
  :repl-options {:init-ns sockeye.core}
  :source-paths      ["src/clj"]
  :java-source-paths ["src/java"]
  :javac-options     ["-target" "1.8" "-source" "1.8"]
  :profiles {:dev {:dependencies [[cheshire "5.8.1"]
                                  [http-kit "2.3.0"]]}})
