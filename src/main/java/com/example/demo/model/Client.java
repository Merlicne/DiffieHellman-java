package com.example.demo.model;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {
    // private key
    // secret key
    // username 

    private String username;
    private BigInteger privateKey;
    private BigInteger secretKey;
}
