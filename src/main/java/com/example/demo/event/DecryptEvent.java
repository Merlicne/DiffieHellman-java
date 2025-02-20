package com.example.demo.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DecryptEvent {
    private String username;
    private String roomId;
    private String message;
    private String messageId;
}
