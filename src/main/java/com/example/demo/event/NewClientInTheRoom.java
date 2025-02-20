package com.example.demo.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.example.demo.model.Client;

@Data
@AllArgsConstructor
@Builder
public class NewClientInTheRoom {
    private String roomId;
    private Client client;
}
