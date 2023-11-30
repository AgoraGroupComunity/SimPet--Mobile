package com.example.simpet01;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class HashMd5 {
    public static String hashMD5(String input) {
        try {
            // Create a MessageDigest with the MD5 algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Convert the input string to bytes
            byte[] inputBytes = input.getBytes();

            // Update the MessageDigest with the input bytes
            md.update(inputBytes);

            // Calculate the MD5 hash
            byte[] digest = md.digest();

            // Convert the hash bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String[] args) {
//        String input = "Hello, MD5!";
//        String hashed = hashMD5(input);
//        System.out.println("Input: " + input);
//        System.out.println("MD5 Hash: " + hashed);
//    }
}
