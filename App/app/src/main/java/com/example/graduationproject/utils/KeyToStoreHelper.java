package com.example.graduationproject.utils;

import android.content.Context;

import com.example.graduationproject.data.local.PrivateKeyToStore;
import com.example.graduationproject.data.local.PublicKeyToStore;
import com.example.graduationproject.exception.MyException;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPublicKeyParameters;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;

public class KeyToStoreHelper {
    private static final String PRIVATE_FILE_NAME = "private.dat";
    private static final String PUBLIC_FILE_NAME = "public.dat";

    private static final String[] dilithiumParameterList = {""};
    public static void generateDilithiumKeyPair(String keyAlias, Context context, String dilithiumParametersType) throws MyException {

        // generate dilithium key pair
        AsymmetricCipherKeyPair keyPair = DilithiumHelper.generateKeyPair(dilithiumParametersType);

        // extract private, public key
        DilithiumPrivateKeyParameters privateKeyParameters = (DilithiumPrivateKeyParameters)keyPair.getPrivate();
        DilithiumPublicKeyParameters publicKeyParameters = (DilithiumPublicKeyParameters)keyPair.getPublic();

        // work with keystore
        RSAHelper.generateKeyPair();
        PrivateKey privateKeyStoreKey = RSAHelper.getPrivateKey();
        PublicKey publicKeyStoreKey = RSAHelper.getPublicKey();

        // encrypt dilithium private key in key store
        byte[] encryptedPrivateKeyStoreKey = RSAEncryptor.encryptData(privateKeyParameters.getEncoded(), publicKeyStoreKey);

        // create key object holder to store to file
        UUID uuid = UUID.randomUUID();
        PrivateKeyToStore privateKeyToStore = new PrivateKeyToStore(encryptedPrivateKeyStoreKey, uuid, keyAlias, dilithiumParametersType);
        PublicKeyToStore publicKeyToStore = new PublicKeyToStore(publicKeyParameters.getEncoded(), uuid, keyAlias, dilithiumParametersType);

        FileHelper.writeJsonKeyToFile(privateKeyToStore, context.getApplicationContext(), PRIVATE_FILE_NAME);
        FileHelper.writeJsonKeyToFile(publicKeyToStore, context.getApplicationContext(), PUBLIC_FILE_NAME);
    }
}
