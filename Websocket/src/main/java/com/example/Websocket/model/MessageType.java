package com.example.Websocket.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {
    REGISTER,
    CHAT,
    CLOSE;

    @JsonCreator
    public static MessageType fromValue(String value) {
        return MessageType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}

