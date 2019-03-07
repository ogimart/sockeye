<img src="./sockeye.jpg" title="Sockeye Salmon" padding="20px" width="120" height="120" align="left"/>
# Sockeye

Reconnecting Websocket Client Library for Clojure


## Description

Sockeye is a standalone reconnectiong websocket client for Clojure. It's based on [Tyrus](https://tyrus-project.github.io), a reference implementation of [javax.websockets](https://docs.oracle.com/javaee/7/api/javax/websocket/package-summary.html) API. It supports automatic reconnecting on disconnects and failures.

***Warning:*** This is work in progress, and API is subject to change until version 1.0.

## Usage

### Dependency

Latest version: `0.1.0-alpha`
Note: not yet released to clojars

deps.edn:
```clj
ogimart/sockeye {:mvn/version "0.1.0-alpha"}
```

leiningen:
```clj
:dependencies [[ogimart/sockeye "0.1.0-alpha"]]
```

maven:
```xml
<dependency>
  <groupId>ogimart</groupId>
  <artifactId>sockeye</artifactId>
  <version>0.1.0-alpha</version>
</dependency>
```

### Minimal example:
```clojure
(require '[ogimart/sockeye.core :refer :all])

(def url "ws://localhost:9090")
(def clt (client))
(def listener (event-handler {:on-open (fn [sess]
                                         (println "connected"))
                              :on-close (fn [code reason]
                                          (println "connection closed:" code reason))
                              :on-text (fn [msg]
                                         (println msg))}))

(def session (connect url clt listener))
(send-text session "text message")
(disconnect session)
```

## API

- `(client & options)`
  options:
  - `:on-disconnect`
  - `:on-connect-failure`
  - `:delay`
- `(event-handler url & options)`
  options:
  - `:on-open`
  - `:on-close`
  - `:on-message`
  - `:on-error`
- `(connect url client handler)`
- `(disconnect socket)`
- `(send-message socket message)`

## TODO

- [ ] deploy to clojars
- [X] open and close websocket connection
- [X] receive and send text messages
- [ ] receive and send binary messages
- [X] reconnecting client
- [X] [component/Lifecycle](https://github.com/stuartsierra/component) integration
- [ ] full example
- [ ] set headears
- [ ] set subprotocols
- [ ] set subprotocol extensions
- [ ] tests with local server
- [ ] connecting through proxy
- [ ] async integration

## Change Log

[Change log](./CHANGELOG.md) is availabe on GitHub.

## License

Copyright © 2019 Ogi Martinovic

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
