package com.example.demo.event;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemMessage {
    private String content; // The announcement text
    private String roomId; // The chat room ID
    private LocalDateTime timestamp; // Timestamp of the announcement
    private int n_clients; // Number of clients in the room
}

