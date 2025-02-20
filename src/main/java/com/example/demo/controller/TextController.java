package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TextController {

    @GetMapping("/text")
    public String showTextPage(Model model) {
        // Example encrypted text and decrypted text
        String encryptedText = "U2FsdGVkX19RbnRlbXBNb2NrRGF0YQ==";  // Simulated encrypted text
        String decryptedText = "Hello, this is decrypted!";  // Simulated decrypted text

        // Add attributes to the model for Thymeleaf
        model.addAttribute("encryptedText", encryptedText);
        model.addAttribute("decryptedText", decryptedText);
        
        return "textPage"; // This points to the Thymeleaf template "textPage.html"
    }
}
