package com.example.keystorelearning;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

public class RSADecryptor {
    public static byte[] decryptData(byte[] encryptedData, PrivateKey privateKey) {
        try {
            OAEPParameterSpec spec = new OAEPParameterSpec(
                    "SHA-256", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey, spec);

            if (encryptedData.length <= 512) {
                return cipher.doFinal(encryptedData);
            }

            // blockSize = keySize - 2 x hashSize -2, use sha-256 => hashSize = 32
            // => blockSize = 4096/8 - 2 * 32 - 2 = 446 (byte)
            int blockSize = 512;

            // ByteArrayOutputStream work with changeable array, instead of byte array only work with fixed array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            for (int i = 0; i < encryptedData.length; i += blockSize) {
                int end = Math.min(encryptedData.length, i + blockSize);
                byte[] block = Arrays.copyOfRange(encryptedData, i, end);
                byte[] decryptedBlock = cipher.doFinal(block);
                outputStream.write(decryptedBlock);
            }

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("failed to decrypt", e);
        }
    }
}
