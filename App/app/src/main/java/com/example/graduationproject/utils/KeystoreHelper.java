package com.example.graduationproject.utils;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import com.example.graduationproject.config.MyConstant;

import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.Arrays;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

public class KeystoreHelper {
    private static KeyPair keyPair;
    private static final String KEY_ALIAS = MyConstant.KEY_STORE_KEY_ALIAS;

    private static final String ANDROID_KEY_STORE = MyConstant.ANDROID_KEY_STORE;

    public static void generateKeyPair() {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);

            // WARNING: FOR TESTING PURPOSES ONLY!!
            // DO NOT UNCOMMENT THE FOLLOWING CODE BLOCK UNLESS YOU FULLY UNDERSTAND ITS PURPOSE AND CONSEQUENCES.
            // Uncommenting this code without understanding may lead to unexpected behavior, data loss, or security vulnerabilities.
            // If you need to uncomment this code, ensure that you thoroughly understand its functionality and implications, and proceed with caution.
//             clearKeyStore();

            displayKeyStoreEntries();

            // check if the key pair with the given alias already exists
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                Log.d("Keystore Entries", "create new key pair in key store");

                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                        KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEY_STORE);

                keyPairGenerator.initialize(
                        new KeyGenParameterSpec.Builder(KEY_ALIAS,
                                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                                .setKeySize(4096)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                                .build());

                keyPair = keyPairGenerator.generateKeyPair();
            } else {
                // load the existing key pair
                Log.d("Keystore Entries", "load the existing key pair in key store");
                keyPair = new KeyPair(
                        keyStore.getCertificate(KEY_ALIAS).getPublicKey(),
                        (PrivateKey) keyStore.getKey(KEY_ALIAS, null));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static PublicKey getPublicKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);
            return keyStore.getCertificate(KEY_ALIAS).getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("failed to get key store public key", e);
        }
    }
    public static PrivateKey getPrivateKey() {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);
            return (PrivateKey) keyStore.getKey(KEY_ALIAS, null);
        } catch (Exception e) {
            throw new RuntimeException("failed to get key store private key", e);
        }
    }
    // delete all current entries in the key store
    public static void clearKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);

            Enumeration<String> aliases = keyStore.aliases();

            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                keyStore.deleteEntry(alias);
                Log.d("Keystore Entries", "successfully deleted alias in key store: " + alias);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete entries in keystore", e);
        }
    }
    public static void displayKeyStoreEntries() {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);

            Enumeration<String> aliases = keyStore.aliases();

            if (!aliases.hasMoreElements()) {
                Log.d("Keystore Entries", "No entries found in keystore");
            } else {
                while (aliases.hasMoreElements()) {
                    String alias = aliases.nextElement();
                    Log.d("Keystore Entries", "Alias: " + alias);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to display entries in keystore", e);
        }
    }
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
