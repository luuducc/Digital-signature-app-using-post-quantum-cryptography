package com.example.keystorelearning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPrivateKeyParameters;
import org.bouncycastle.pqc.crypto.crystals.dilithium.DilithiumPublicKeyParameters;

import java.util.List;

import com.example.keystorelearning.keytostore.KeyToStoreHelper;
import com.example.keystorelearning.keytostore.PrivateKeyToStore;
import com.example.keystorelearning.keytostore.PublicKeyToStore;
import com.example.keystorelearning.rsakeystore.RSADecryptor;
import com.example.keystorelearning.rsakeystore.RSAHelper;
import com.example.keystorelearning.util.DilithiumHelper;
import com.example.keystorelearning.util.FileHelper;

public class MainActivity extends AppCompatActivity {
    private TextView txtView;
    private final String dilithiumParametersType = "dilithium2";
    private final String PRIVATE_FILE_NAME = "private.dat";
    private final String PUBLIC_FILE_NAME = "public.dat";
    private final String KEY_ALIAS = "fourth_key";
    private final int position = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.keyRecyclerView);

        txtView = findViewById(R.id.textView);

//         Click button to generate key pair
        KeyToStoreHelper.generateDilithiumKeyPair(KEY_ALIAS, getApplicationContext(), dilithiumParametersType);

        // retrieve all keys from internal storage to display to recycler view
        List<PrivateKeyToStore> retrievedPrivateKeys = FileHelper.retrievePrivateKeyFromFile(getApplicationContext(), PRIVATE_FILE_NAME);
        List<PublicKeyToStore> retrievedPublicKeys = FileHelper.retrievePublicKeyFromFile(getApplicationContext(), PUBLIC_FILE_NAME);

        // assume that user pick the specific key pair from the list shown in recycler view
//        PrivateKeyToStore privateKeyToStore = retrievedPrivateKeys.get(position);
//        PublicKeyToStore publicKeyToStore = retrievedPublicKeys.get(position);
//        byte[] encryptedPrivateKeyByte = privateKeyToStore.getEncryptedPrivateKey();
//        byte[] privateKeyByte = RSADecryptor.decryptData(encryptedPrivateKeyByte, RSAHelper.getPrivateKey());
//        byte[] publicKeyByte = publicKeyToStore.getPublicKey();
//
//
//        DilithiumPublicKeyParameters publicKeyParameters = DilithiumHelper.retrievePublicKey(privateKeyToStore.getDilithiumParametersType(), publicKeyByte);
//        DilithiumPrivateKeyParameters privateKeyParameters = DilithiumHelper.retrievePrivateKey(publicKeyToStore.getDilithiumParametersType(), privateKeyByte, publicKeyParameters);
//
//        byte[] data = "".getBytes();
//
//        byte[] signedMessage = DilithiumHelper.sign(privateKeyParameters, data);
//        boolean verifyResult = DilithiumHelper.verify(publicKeyParameters, data, signedMessage);
//
//        txtView.setText(Boolean.toString(verifyResult));

        Key_RecyclerViewAdapter adapter = new Key_RecyclerViewAdapter(this, retrievedPublicKeys);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}