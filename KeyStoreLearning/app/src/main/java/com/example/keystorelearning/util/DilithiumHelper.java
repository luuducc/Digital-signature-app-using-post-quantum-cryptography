package com.example.keystorelearning.util;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumKeyGenerationParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumKeyPairGenerator;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPublicKeyParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumSigner;

import java.security.SecureRandom;

public class DilithiumHelper {

    public static AsymmetricCipherKeyPair generateKeyPair(String parameterType) {
        DilithiumParameters parameters = getParametersType(parameterType);

        SecureRandom random = new SecureRandom();
        DilithiumKeyGenerationParameters genParam = new DilithiumKeyGenerationParameters(random, parameters);
        DilithiumKeyPairGenerator keyPairGenerator = new DilithiumKeyPairGenerator();
        keyPairGenerator.init(genParam);
        AsymmetricCipherKeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    public static byte[] sign(DilithiumPrivateKeyParameters privateKeyParameters, byte[] hashedData) {
        DilithiumSigner signer = new DilithiumSigner();
        signer.init(privateKeyParameters.isPrivate(), privateKeyParameters);
        return signer.generateSignature(hashedData);
    }

    public static boolean verify(DilithiumPublicKeyParameters publicKeyParameters, byte[] initialHashedData, byte[] signedMessage) {
        DilithiumSigner verifySigner = new DilithiumSigner();
        verifySigner.init(publicKeyParameters.isPrivate(), publicKeyParameters);
        return verifySigner.verifySignature(initialHashedData, signedMessage);
    }

    public static DilithiumPrivateKeyParameters retrievePrivateKey(String parametersType, byte[] privateEncoded, DilithiumPublicKeyParameters publicKeyParameters) {
        return new DilithiumPrivateKeyParameters(getParametersType(parametersType), privateEncoded, publicKeyParameters);
    }

    public static DilithiumPublicKeyParameters retrievePublicKey(String parametersType, byte[] publicEncoded) {
        return new DilithiumPublicKeyParameters(getParametersType(parametersType), publicEncoded);
    }

    private static DilithiumParameters getParametersType(String parametersType){
        DilithiumParameters parameters;
        switch (parametersType){
            case "dilithium2": parameters = DilithiumParameters.dilithium2; break;
            case "dilithium3": parameters = DilithiumParameters.dilithium3; break;
            case "dilithium5": parameters = DilithiumParameters.dilithium5; break;
            default: parameters = null;
        }
        return parameters;
    }
}
