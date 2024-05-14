//import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
//import org.bouncycastle.pqc.crypto.crystals.dilithium.*;
//
//import java.security.SecureRandom;
//
//public class SignMessage {
//
//    public byte[] sign(byte[] message) {
//
//        SecureRandom random = new SecureRandom();
//
//        // Create keypair generator parameter
//        DilithiumKeyGenerationParameters genParam = new DilithiumKeyGenerationParameters(random, DilithiumParameters.dilithium5);
//
//        // Initialize keypair generator
//        DilithiumKeyPairGenerator keyPairGenerator = new DilithiumKeyPairGenerator();
//        keyPairGenerator.init(genParam);
//
//        // Generate the keypair
//        AsymmetricCipherKeyPair keyPair = keyPairGenerator.generateKeyPair();
//
//        // Get the private key
//        DilithiumPrivateKeyParameters privateKeyParameters = (DilithiumPrivateKeyParameters)keyPair.getPrivate();
//
//        // Initialize the signer
//        DilithiumSigner signer = new DilithiumSigner();
//        signer.init(privateKeyParameters.isPrivate(), privateKeyParameters);
//
//        byte[] signedMessage = signer.generateSignature(message);
//
//        return signedMessage;
//    }
//
//    public boolean verifySinature(byte[] hashMessage, byte[] message) {
//        // Get the public key
//        DilithiumPublicKeyParameters publicKeyParameters = (DilithiumPublicKeyParameters)keyPair.getPublic();
//
//        // Initialize verify signer
//        DilithiumSigner verifySigner = new DilithiumSigner();
//        verifySigner.init(publicKeyParameters.isPrivate(), publicKeyParameters);
//    }
//}
