package com.example.demo.service;


import java.math.BigInteger;

import com.example.demo.model.Client;


public interface  DiffieHellman {
    public BigInteger generatePublicKey(Client client);
    public void generateSecretKey(Client client, BigInteger genKey);
    public String encrypt(Client client, String message);
    public String decrypt(Client client, String message);
    public BigInteger getP();
    public BigInteger getG();
}