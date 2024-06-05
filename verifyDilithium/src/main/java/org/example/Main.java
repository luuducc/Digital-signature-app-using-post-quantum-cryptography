package org.example;

import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPublicKeyParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumSigner;

import java.util.Base64;

public class Main {
    public static void main(String[] args) {
        // Get the parameters from command line
        String parameterType = args[0];
        String publicKeyStr = args[1];
        String signatureString = args[2];
        String initialHashedMessage = args[3];

        // Decode the base64 encoded strings
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
        byte[] signedMessage = Base64.getDecoder().decode(signatureString);
        byte[] hash = Base64.getDecoder().decode(initialHashedMessage);

        // my logic
        DilithiumPublicKeyParameters publicKeyParameters = retrievePublicKey(parameterType, publicKeyBytes);
        boolean verifyResult = verify(publicKeyParameters, hash, signedMessage);
        System.out.println(Boolean.toString(verifyResult));
    }

    public static boolean verify(DilithiumPublicKeyParameters publicKeyParameters, byte[] initialHashedData, byte[] signature) {
        DilithiumSigner verifySigner = new DilithiumSigner();
        verifySigner.init(publicKeyParameters.isPrivate(), publicKeyParameters);
        return verifySigner.verifySignature(initialHashedData, signature);
    }

    public static DilithiumPublicKeyParameters retrievePublicKey(String parametersType, byte[] publicEncoded) {
        return new DilithiumPublicKeyParameters(getParametersType(parametersType), publicEncoded);
    }
    private static DilithiumParameters getParametersType(String parametersType){
        DilithiumParameters parameters;
        switch (parametersType) {
            case "dilithium2":
                parameters = DilithiumParameters.dilithium2;
                break;
            case "dilithium3":
                parameters = DilithiumParameters.dilithium3;
                break;
            case "dilithium5":
                parameters = DilithiumParameters.dilithium5;
                break;
            default:
                parameters = null;
                break;
        }
        return parameters;
    }
}