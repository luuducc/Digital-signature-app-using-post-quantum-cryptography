package com.example.graduationproject.utils;

import com.example.graduationproject.config.MyConstant;
import com.example.graduationproject.exception.MyException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashHelper {
    public static byte[] hashString(String input) {
        try {
            byte[] encodedHash = calculateHash(input.getBytes());
            return encodedHash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hashPDF(String className) throws MyException {
        String pdfFolderPath = MyConstant.GRADUATION_PROJECT_FOLDER + "/Transcripts";

        File file = new File(pdfFolderPath, className + ".pdf");
        if (!file.exists()) {
            throw new MyException("Pdf file is not created: " + className);
        }

        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            fis.close();
            byte[] hash = calculateHash(fileBytes);
            return hash;
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new MyException("Failed to hash pdf file");
        }

    }

    // Helper method to convert byte array to hex string
    public static byte[] calculateHash(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        return messageDigest.digest(data);
    }
}
