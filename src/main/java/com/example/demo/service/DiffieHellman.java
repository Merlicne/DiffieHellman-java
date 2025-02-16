package com.example.demo.service;


import org.springframework.stereotype.Service;


@Service
public interface  DiffieHellman {

    public String generatePublicKey(String base, String modulus, String tempPrivate);
    public String encrypt(String message);
    public String decrypt(String message);
}