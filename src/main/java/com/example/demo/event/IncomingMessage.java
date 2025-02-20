package com.example.demo.event;

import com.example.demo.model.Client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class IncomingMessage {

    private String message;
    private String username;
    private String roomId;
    
}
