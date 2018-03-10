package ua.in.smartjava.websocket;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.*;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import ua.in.smartjava.spark.Gateway;

@WebSocket
public class EventWebSocket {

    @OnWebSocketConnect
    public void connected(Session session) {
        Gateway.sessions.add(session);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        Gateway.sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        session.getRemote().sendString(message);
    }

}
