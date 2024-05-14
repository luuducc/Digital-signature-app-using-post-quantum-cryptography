import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumKeyGenerationParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumKeyPairGenerator;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumKeyParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPublicKeyParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumSigner;

import java.security.SecureRandom;
public class Main {
    public static void main(String[] args) {
        // SIGN

        SecureRandom random = new SecureRandom();

        // Create keypair generator parameter
        DilithiumKeyGenerationParameters genParam = new DilithiumKeyGenerationParameters(random, DilithiumParameters.dilithium5);

        // Initialize keypair generator
        DilithiumKeyPairGenerator keyPairGenerator = new DilithiumKeyPairGenerator();
        keyPairGenerator.init(genParam);

        // Generate the keypair
        AsymmetricCipherKeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Get the private key
        DilithiumPrivateKeyParameters privateKeyParameters = (DilithiumPrivateKeyParameters)keyPair.getPrivate();

        // Initialize the signer
        DilithiumSigner signer = new DilithiumSigner();
        signer.init(privateKeyParameters.isPrivate(), privateKeyParameters);

        byte[] hashResult = null;
        String filePath = "C:\\Users\\luuduc\\OneDrive - Hanoi University of Science and Technology\\Desktop\\blank.pdf";

        // Hash the message
        hashResult = HashFile.hashFile(filePath);

        // Sign the message
        byte[] signedMessage = signer.generateSignature(hashResult);

        // VERIFY

        // Get the public key
        DilithiumPublicKeyParameters publicKeyParameters = (DilithiumPublicKeyParameters)keyPair.getPublic();

        // Initialize verify signer
        DilithiumSigner verifySigner = new DilithiumSigner();
        verifySigner.init(publicKeyParameters.isPrivate(), publicKeyParameters);

        // Re-hash the message
        byte[] hashToVerify = null;
        hashToVerify = HashFile.hashFile(filePath);

        // Verify the sign
        boolean result = verifySigner.verifySignature(hashToVerify, signedMessage);

        // Log the result
        System.out.println(result);

    }

}