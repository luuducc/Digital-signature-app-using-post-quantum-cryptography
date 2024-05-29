package com.example.keystorelearning.rsakeystore;

import java.io.ByteArrayOutputStream;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

public class RSAEncryptor {
    public static byte[] encryptData(byte[] data, PublicKey publicKey)  {
        try {
            OAEPParameterSpec spec = new OAEPParameterSpec(
                    "SHA-256", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey, spec);

            if (data.length <= 512) {
                return cipher.doFinal(data);
            }

            // blockSize = keySize - 2 x hashSize -2, use sha-256 => hashSize = 32
            // => blockSize = 4096/8 - 2 * 32 - 2 = 446 (byte)
            int blockSize = 446;

            // ByteArrayOutputStream work with changeable array, instead of byte array only work with fixed array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            for (int i = 0; i < data.length; i += blockSize) {
                int end = Math.min(data.length, i + blockSize);
                byte[] block = Arrays.copyOfRange(data, i, end);
                byte[] encryptedBlock = cipher.doFinal(block);
                outputStream.write(encryptedBlock);
            }

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("failed to encrypt", e);
        }
    }
}
