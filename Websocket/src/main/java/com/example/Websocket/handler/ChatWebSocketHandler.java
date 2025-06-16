package com.example.Websocket.handler;

import com.example.Websocket.model.WebSocketMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    private final Map<String, List<String>> userMessages = new ConcurrentHashMap<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Nothing needed yet
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        WebSocketMessage msg = mapper.readValue(message.getPayload(), WebSocketMessage.class);
        System.out.println("Received raw message: " + message.getPayload());
        System.out.println("Deserialized object: " + msg);
        switch (msg.getType()) {
            case REGISTER:
                userSessions.put(msg.getUsername(), session);
                userMessages.putIfAbsent(msg.getUsername(), new ArrayList<>());
                session.sendMessage(new TextMessage("User registered: " + msg.getUsername()));
                break;

            case CHAT:
                String username = getUsernameBySession(session);
                if (username == null) {
                    session.sendMessage(new TextMessage("Error: User not registered"));
                    return;
                }
                userMessages.get(username).add(msg.getMessage());
                session.sendMessage(new TextMessage("Message stored"));
                break;

            case CLOSE:
                String userToRemove = getUsernameBySession(session);
                if (userToRemove != null) {
                    userSessions.remove(userToRemove);
                    session.sendMessage(new TextMessage("User unregistered: " + userToRemove));
                }
                session.close();
                break;

            default:
                session.sendMessage(new TextMessage("Error: Unknown type"));
        }
    }

    private String getUsernameBySession(WebSocketSession session) {
        return userSessions.entrySet().stream()
                .filter(e -> e.getValue().equals(session))
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    }

    public List<String> getMessagesForUser(String username) {
        return userMessages.getOrDefault(username, List.of());
    }
}

