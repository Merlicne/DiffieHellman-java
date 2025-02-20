package com.example.demo.model;

import java.math.BigInteger;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatRoom {
    private String roomId;
    private List<Client> clients;
    private BigInteger G;
    private BigInteger P;
    private List<Message> messages;

    public void addClient(String roomId, Client client) {
        clients.add(client);
    }

    // add message to the chat room
    public void addMessage(String roomId, Message message) {
        messages.add(message);
    }
}
