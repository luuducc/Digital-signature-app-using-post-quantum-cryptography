package com.example.graduationproject.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashHelper {
    public static byte[] hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");

            byte[] encodedHash = digest.digest(input.getBytes());

            return encodedHash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
