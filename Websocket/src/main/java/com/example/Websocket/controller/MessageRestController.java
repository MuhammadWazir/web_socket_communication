package com.example.Websocket.controller;

import com.example.Websocket.handler.ChatWebSocketHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messages", description = "Endpoints for retrieving chat messages")
public class MessageRestController {

    private final ChatWebSocketHandler chatWebSocketHandler;

    public MessageRestController(ChatWebSocketHandler chatWebSocketHandler) {
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @Operation(summary = "Retrieve all messages for a given username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Username not found or not registered")
    })
    @GetMapping("/{username}")
    public ResponseEntity<List<String>> getMessagesByUsername(@PathVariable String username) {
        List<String> messages = chatWebSocketHandler.getMessagesForUser(username);

        if (messages.isEmpty()) {
            return ResponseEntity.notFound().build(); // username not found or no messages
        }

        return ResponseEntity.ok(messages);
    }
}
