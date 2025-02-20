package com.example.demo.controller;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.event.DecryptEvent;
import com.example.demo.event.IncomingMessage;
import com.example.demo.event.KeyCalculated;
import com.example.demo.event.SystemMessage;
import com.example.demo.model.ChatForm;
import com.example.demo.model.ChatRoom;
import com.example.demo.model.Client;
import com.example.demo.model.Message;
import com.example.demo.service.DiffieHellman;
import com.example.demo.service.NumberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final DiffieHellman diffieHellman; // P, G
    private final NumberService numberService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final Map<String, ChatRoom> chatRooms = new HashMap<>();

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("chatForm", ChatForm.builder().build());
        return "index";
    }

    // enter chat room
    @PostMapping("/chat")
    public String chat(@ModelAttribute ChatForm chatForm, Model model) {
        String username = chatForm.getUsername();
        String chatRoom_id = chatForm.getChatRoom_id();

        ChatRoom chatRoom;
        if (!chatRooms.containsKey(chatRoom_id)) {
            BigInteger P = numberService.generatePrime(15);
            chatRoom = ChatRoom.builder()
                    .roomId(chatRoom_id)
                    .P(P)
                    .G(numberService.primativeRoot(P))
                    .clients(new ArrayList<>())
                    .messages(new ArrayList<>())
                    .build();
            chatRooms.put(chatRoom_id, chatRoom);
        } else {
            chatRoom = chatRooms.get(chatRoom_id);
        }

        if (chatRoom.getClients().size() >= 2) {
            model.addAttribute("error", "Chat room is full!");
            return "index";
        }

        Client client = chatRoom.getClients().stream()
                .filter(c -> c.getUsername().equals(username))
                .findFirst()
                .orElseGet(() -> {
                    Client newClient = Client.builder()
                            .username(username)
                            .privateKey(new BigInteger(16, new Random()))
                            .build();
                    chatRoom.getClients().add(newClient);
                    return newClient;
                });

        model.addAttribute("chatRoom", chatRoom);
        BigInteger keyGenerated = diffieHellman.generateKey(client, chatRoom.getG(), chatRoom.getP());
        model.addAttribute("keyGen", keyGenerated);
        model.addAttribute("client", client);
        return "chat";
    }

    @MessageMapping("/keyExchange")
    private void keyExchange(String chatRoomId) {
        ChatRoom chatRoom = chatRooms.get(chatRoomId);

        if (chatRoom.getClients().size() != 2) {
            sendSystemMessage(SystemMessage.builder()
                    .content("Waiting for the other user to join")
                    .roomId(chatRoomId)
                    .timestamp(LocalDateTime.now())
                    .build());
            return;
        }


        List<Client> clients = chatRoom.getClients();
        BigInteger key1 = diffieHellman.generateKey(clients.get(0), chatRoom.getG(), chatRoom.getP());
        BigInteger key2 = diffieHellman.generateKey(clients.get(1), chatRoom.getG(), chatRoom.getP());

        String secret1 = diffieHellman.generateSecretKey(clients.get(1), key1 , chatRoom.getP());
        String secret2 = diffieHellman.generateSecretKey(clients.get(0), key2 , chatRoom.getP());

        clients.get(0).setSecretKey(new BigInteger(secret1));
        clients.get(1).setSecretKey(new BigInteger(secret2));
        
        KeyCalculated k1 = KeyCalculated.builder()
        .username(clients.get(0).getUsername())
        .roomId(chatRoom.getRoomId())
        .otherKey(key2.toString())
        .secretKey(clients.get(0).getSecretKey().toString())
        .build();
        
        KeyCalculated k2 = KeyCalculated.builder()
        .username(clients.get(1).getUsername())
        .roomId(chatRoom.getRoomId())
        .otherKey(key1.toString())
        .secretKey(clients.get(1).getSecretKey().toString())
        .build();
        
        simpMessagingTemplate.convertAndSend("/topic/keys/" + chatRoom.getRoomId() + "/" + clients.get(0).getUsername(), k1);
        simpMessagingTemplate.convertAndSend("/topic/keys/" + chatRoom.getRoomId() + "/" + clients.get(1).getUsername(), k2);
        
        sendSystemMessage(SystemMessage.builder()
                .content("Key exchange completed")
                .roomId(chatRoom.getRoomId())
                .timestamp(LocalDateTime.now())
                .build());
        
    }
    

    private void sendSystemMessage(SystemMessage systemMessage) {
        simpMessagingTemplate.convertAndSend("/topic/systemMessages", systemMessage);
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public Message send(IncomingMessage incomingMessage) {
        ChatRoom chatRoom = chatRooms.get(incomingMessage.getRoomId());
        Client client = chatRoom.getClients().stream()
                .filter(c -> c.getUsername().equals(incomingMessage.getUsername()))
                .findFirst()
                .orElseThrow();

        String encryptedMessage = incomingMessage.getMessage();
        if (client.getSecretKey() != null) {
            encryptedMessage = diffieHellman.encrypt(client, encryptedMessage);
        }

        Message message = Message.builder()
                .message(encryptedMessage)
                .sender(client)
                .roomId(incomingMessage.getRoomId())
                .timestamp(LocalDateTime.now())
                .build();

        chatRoom.getMessages().add(message);
        return message;
    }

    @MessageMapping("/system")
    @SendTo("/topic/systemMessages")
    public SystemMessage handleSystemMessage(SystemMessage systemMessage) {
        systemMessage.setTimestamp(LocalDateTime.now());
        systemMessage.setN_clients(chatRooms.get(systemMessage.getRoomId()).getClients().size());
        return systemMessage;
    }

    @MessageMapping("/decrypt")
    public void decrypt(DecryptEvent decryptEvent) {
        ChatRoom chatRoom = chatRooms.get(decryptEvent.getRoomId());
        Client client = chatRoom.getClients().stream()
                .filter(c -> c.getUsername().equals(decryptEvent.getUsername()))
                .findFirst()
                .orElseThrow();

        System.out.println("Decrypting message: " + decryptEvent.getMessage());
        String decryptedMessage = diffieHellman.decrypt(client, decryptEvent.getMessage());
        DecryptEvent decryptedMessageEvent = DecryptEvent.builder()
                .roomId(decryptEvent.getRoomId())
                .username(decryptEvent.getUsername())
                .message(decryptedMessage)
                .messageId(decryptEvent.getMessageId())
                .build();
        simpMessagingTemplate.convertAndSend("/topic/decrypted/" + decryptEvent.getRoomId() + "/" + decryptEvent.getUsername(), decryptedMessageEvent);
    }


}
