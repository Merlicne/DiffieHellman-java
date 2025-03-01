package com.example.demo.service;

import java.math.BigInteger;

import org.apache.logging.log4j.util.StringBuilders;

import com.example.demo.model.Client;

public interface DiffieHellman {
	public BigInteger generateKey(Client client, BigInteger G, BigInteger P);

	public String generateSecretKey(Client client, BigInteger genKey, BigInteger P);

	public String encrypt(Client client, String message);

	public String decrypt(Client client, String message);
}
