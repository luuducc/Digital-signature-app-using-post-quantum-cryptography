package com.example.keystorelearning.test;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.CipherOutputStream;
import javax.crypto.CipherInputStream;

public class CryptoManager {
    private static final String ALGORITHM = KeyProperties.KEY_ALGORITHM_AES;
    private static final String BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC;
    private static final String PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7;
    private static final String TRANSFORMATION = ALGORITHM + "/" + BLOCK_MODE + "/" + PADDING;

    private KeyStore keyStore;

    public CryptoManager() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize KeyStore", e);
        }
    }

    private Cipher getEncryptCipher() {
        try {
            Cipher encryptCipher = Cipher.getInstance(TRANSFORMATION);
            encryptCipher.init(Cipher.ENCRYPT_MODE, getKey());
            return encryptCipher;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Cipher for encryption", e);
        }
    }

    private Cipher getDecryptCipherForIv(byte[] iv) {
        try {
            Cipher decryptCipher = Cipher.getInstance(TRANSFORMATION);
            decryptCipher.init(Cipher.DECRYPT_MODE, getKey(), new IvParameterSpec(iv));
            return decryptCipher;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Cipher for decryption", e);
        }
    }

    private SecretKey getKey() {
        try {
            KeyStore.SecretKeyEntry existingKey = (KeyStore.SecretKeyEntry) keyStore.getEntry("secret", null);
            return existingKey != null ? existingKey.getSecretKey() : createKey();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get or create the secret key", e);
        }
    }

    private SecretKey createKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder("secret",
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build());
            return keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create the secret key", e);
        }
    }

    public byte[] encrypt(byte[] bytes, OutputStream outputStream) {
        try {
            Cipher encryptCipher = getEncryptCipher();
            byte[] encryptedBytes = encryptCipher.doFinal(bytes);
            outputStream.write(encryptCipher.getIV().length);
            outputStream.write(encryptCipher.getIV());
            outputStream.write(encryptedBytes.length);
            outputStream.write(encryptedBytes);
            return encryptedBytes;
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt data", e);
        }
    }

    public byte[] decrypt(InputStream inputStream) {
        try {
            int ivSize = inputStream.read();
            byte[] iv = new byte[ivSize];
            inputStream.read(iv);

            int encryptedBytesSize = inputStream.read();
            byte[] encryptedBytes = new byte[encryptedBytesSize];
            inputStream.read(encryptedBytes);

            return getDecryptCipherForIv(iv).doFinal(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt data", e);
        }
    }

    public void encrypt(InputStream inputStream, OutputStream outputStream) {
        try {
            Cipher encryptCipher = getEncryptCipher();
            outputStream.write(encryptCipher.getIV().length);
            outputStream.write(encryptCipher.getIV());

            try (CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, encryptCipher)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    cipherOutputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt data", e);
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    public void decrypt(InputStream inputStream, OutputStream outputStream) {
        try {
            int ivSize = inputStream.read();
            byte[] iv = new byte[ivSize];
            inputStream.read(iv);

            Cipher decryptCipher = getDecryptCipherForIv(iv);
            try (CipherInputStream cipherInputStream = new CipherInputStream(inputStream, decryptCipher)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt data", e);
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }
}
