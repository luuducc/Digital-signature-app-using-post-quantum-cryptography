package com.example.keystorelearning;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.widget.TextView;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumKeyGenerationParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumKeyPairGenerator;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPublicKeyParameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import  com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {
    private TextView txtView;
    private final String dilithiumParametersType = "dilithium3";
    private final String PRIVATE_FILE_NAME = "private.dat";
    private final String PUBLIC_FILE_NAME = "public.dat";
    private final String KEY_ALIAS = "third_key";
    private final int position = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtView = findViewById(R.id.textView);

//         Click button to generate key pair
        KeyToStoreHelper.generateDilithiumKeyPair(KEY_ALIAS, getApplicationContext(), dilithiumParametersType);

        // retrieve all keys from internal storage to display to recycler view
        List<PrivateKeyToStore> retrievedPrivateKeys = FileHelper.retrievePrivateKeyFromFile(getApplicationContext(), PRIVATE_FILE_NAME);
        List<PublicKeyToStore> retrievedPublicKeys = FileHelper.retrievePublicKeyFromFile(getApplicationContext(), PUBLIC_FILE_NAME);

        // assume that user pick the specific key pair from the list shown in recycler view
        PrivateKeyToStore privateKeyToStore = retrievedPrivateKeys.get(position);
        PublicKeyToStore publicKeyToStore = retrievedPublicKeys.get(position);
        byte[] encryptedPrivateKeyByte = privateKeyToStore.getEncryptedPrivateKey();
        byte[] privateKeyByte = RSADecryptor.decryptData(encryptedPrivateKeyByte, RSAHelper.getPrivateKey());
        byte[] publicKeyByte = publicKeyToStore.getPublicKey();


        DilithiumPublicKeyParameters publicKeyParameters = DilithiumHelper.retrievePublicKey(privateKeyToStore.getDilithiumParametersType(), publicKeyByte);
        DilithiumPrivateKeyParameters privateKeyParameters = DilithiumHelper.retrievePrivateKey(publicKeyToStore.getDilithiumParametersType(), privateKeyByte, publicKeyParameters);

        byte[] data = "".getBytes();

        byte[] signedMessage = DilithiumHelper.sign(privateKeyParameters, data);
        boolean verifyResult = DilithiumHelper.verify(publicKeyParameters, data, signedMessage);

        txtView.setText(Boolean.toString(verifyResult));

    }
}