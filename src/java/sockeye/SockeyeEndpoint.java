package sockeye;

import javax.websocket.*;

public abstract class SockeyeEndpoint extends Endpoint {

    @Override
    public void onOpen(javax.websocket.Session session, EndpointConfig config) {
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                onText(message);
            }
        });
     }

    public abstract void onText(String message);
}
