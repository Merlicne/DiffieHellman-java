package com.example.demo.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.demo.service.NumberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NumberImpl implements NumberService{
    
    @Override
    public BigInteger generatePrime(int n_digits) {
        BigInteger maxNumber = BigInteger.TEN.pow(n_digits).subtract(BigInteger.ONE);

        return BigInteger.probablePrime(maxNumber.bitLength(), new java.util.Random());
        
    }

    @Override
    public BigInteger primativeRoot(BigInteger prime) {
        return primitiveRootOf(prime);
    }

    private static List<BigInteger> primeFactors(BigInteger n) {
        Set<BigInteger> factors = new HashSet<>();
        while (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            factors.add(BigInteger.TWO);
            n = n.divide(BigInteger.TWO);
        }

        for (BigInteger i = BigInteger.valueOf(3); i.multiply(i).compareTo(n) <= 0; i = i.add(BigInteger.TWO)) {
            while (n.mod(i).equals(BigInteger.ZERO)) {
                factors.add(i);
                n = n.divide(i);
            }
        }

        if (n.compareTo(BigInteger.ONE) > 0) {
            factors.add(n);
        }
        // return sorted list
        return new ArrayList<>(factors);
    }

    public static BigInteger primitiveRootOf(BigInteger prime) {
        if (!prime.isProbablePrime(10)) {
            return BigInteger.ZERO; // Not a prime
        }

        BigInteger phi = prime.subtract(BigInteger.ONE);
        System.out.println("Find primitive root of " + prime + " phi: " + phi);
        List<BigInteger> factors = primeFactors(phi);
        System.out.println("Factors: " + factors);

        for (BigInteger r = BigInteger.TWO; r.compareTo(phi) <= 0; r = r.add(BigInteger.ONE)) {
            boolean isPrimitiveRoot = true;

            for (BigInteger factor : factors) {
                if (r.modPow(phi.divide(factor), prime).equals(BigInteger.ONE)) {
                    isPrimitiveRoot = false;
                    break;
                }
            }

            if (isPrimitiveRoot) {
                return r;
            }
        }
        return BigInteger.ZERO;
    }

}
