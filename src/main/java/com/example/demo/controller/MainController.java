package com.example.demo.controller;

import java.math.BigInteger;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.ChatRoom;
import com.example.demo.model.Client;
import com.example.demo.service.DiffieHellman;
import com.example.demo.service.NumberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final DiffieHellman diffieHellman;  // P, G
    private final NumberService numberService;

    @GetMapping("/")
    public String home() {
        return "index";
    }


    // enter chat room
    @PostMapping("/chat")
    public String chat(@RequestParam String username, @RequestParam String chatRoom_id,  Model model) {
        Random randNum = new Random();
		BigInteger privateKey =  new BigInteger(32, randNum); //a,b

        Client client = Client.builder().username(username).privateKey(privateKey).build();
        model.addAttribute("client", client);

        BigInteger P = numberService.generatePrime(5);
        ChatRoom chatRoom = ChatRoom.builder()
                            .roomId(chatRoom_id)
                            .P(P)
                            .G(numberService.primativeRoot(P))
                            .build(); 
        chatRoom.addClient(chatRoom_id, client);
        model.addAttribute("chatRoom", chatRoom);
        
        // a, b
        BigInteger keyGenerated = diffieHellman.generateKey(client, chatRoom.getG(), chatRoom.getP()); //x,y
        model.addAttribute("keyGen", keyGenerated);

        model.addAttribute("P", chatRoom.getP());
        model.addAttribute("G", chatRoom.getG());
        model.addAttribute("pv_key", client.getPrivateKey());
        return "chat";
    }    

    @GetMapping("/chat/{room_id}")
    public String chat(Model model) {
        return "chat";
    }

    




}
