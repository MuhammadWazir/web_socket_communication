package com.example.Websocket.model;

import lombok.Data;

@Data
public class WebSocketMessage {
    private MessageType type;
    private String username;
    private String message;
}

