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

    private final int n_digits ;
    private final BigInteger P ;
    private final BigInteger G ;

    public DiffieImpl(NumberService numberService) {
        this.numberService = numberService;
        this.n_digits = 10;
        this.P = numberService.generatePrime(n_digits);
        this.G = numberService.primativeRoot(P);
    }

    @Override
    public BigInteger generatePublicKey(Client client) {
        // G^a mod P
        BigInteger publicKey  = G.modPow(client.getPrivateKey(), P);
        return publicKey;
    }

    @Override
    public void generateSecretKey(Client client, BigInteger genKey) {
        // genKey^a mod P : 
        // genKey:other user
        client.setSecretKey(genKey.modPow(client.getPrivateKey(), P));
    }

    @Override
    public String encrypt(Client client, String message) {
        TextEncryptor encryptor = Encryptors.text(client.getSecretKey().toString(), "");
        return encryptor.encrypt(message);
    }

    @Override
    public String decrypt(Client client, String message) {
        TextEncryptor encryptor = Encryptors.text(client.getSecretKey().toString(), "");
        return encryptor.decrypt(message);
    }
}
