package com.example.keystorelearning.rsakeystore;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Enumeration;

public class RSAHelper {

    private static KeyPair keyPair;
    private static final String KEY_ALIAS = "graduation_rsa_key";

    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

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
}
