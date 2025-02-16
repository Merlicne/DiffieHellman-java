package com.example.demo.controller;

import java.math.BigInteger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    
    @GetMapping("/")
    public String index() {


        BigInteger p = BigInteger.probablePrime(2048, new java.util.Random());
        BigInteger g = new BigInteger("12389236861231381230817238971");

        BigInteger a = g.mod(p);

        return "BASE36 :" + a.toString(36) + "\nBASE10 :" + a.toString();

    }
}