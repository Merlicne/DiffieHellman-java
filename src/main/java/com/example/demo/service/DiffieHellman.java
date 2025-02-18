package com.example.demo.service;


import java.math.BigInteger;


public interface  DiffieHellman {
    public BigInteger generatePublicKey(BigInteger base, BigInteger modulus, BigInteger clientKey);
    public String encrypt(String message);
    public String decrypt(String message);
}