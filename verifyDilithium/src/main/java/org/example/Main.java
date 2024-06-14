package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPublicKeyParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumSigner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws Exception {
        // new logic
        // get properties from json file
        String inputFilePath = args[0];
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> properties = mapper.readValue(new File(inputFilePath), Map.class);

        String parameterType = properties.get("parameterType");
        String publicKeyStr = properties.get("publicKeyStr");
        String signatureString = properties.get("signatureString");
        String initialHashedMessage = properties.get("initialHashedMessage");

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