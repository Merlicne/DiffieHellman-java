package com.example.demo.service.impl;

import java.math.BigInteger;

import com.example.demo.service.DiffieHellman;
import com.example.demo.service.NumberService;
import com.example.demo.model.Client;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import lombok.Data;
import lombok.Getter;
// import lombok.RequiredArgsConstructor;

@Service
@Getter
@Data
public class DiffieImpl implements DiffieHellman {
    private final NumberService numberService;

    public DiffieImpl(NumberService numberService) {
        this.numberService = numberService;
    }

    @Override
    public BigInteger generateKey(Client client, BigInteger G, BigInteger P) {
        // G^a mod P
        BigInteger keyGenerated = G.modPow(client.getPrivateKey(), P);
        return keyGenerated;
    }

    @Override
    public String generateSecretKey(Client client, BigInteger genKey, BigInteger P) {
        // genKey^a mod P :
        // genKey:other user
        return genKey.modPow(client.getPrivateKey(), P).toString();
    }

    @Override
    public String encrypt(Client client, String message) {
        TextEncryptor encryptor = Encryptors.text(client.getSecretKey().toString(), "555555");
        return encryptor.encrypt(message);
    }

    @Override
    public String decrypt(Client client, String message) {
        TextEncryptor encryptor = Encryptors.text(client.getSecretKey().toString(), "555555");
        return encryptor.decrypt(message);
    }
}
