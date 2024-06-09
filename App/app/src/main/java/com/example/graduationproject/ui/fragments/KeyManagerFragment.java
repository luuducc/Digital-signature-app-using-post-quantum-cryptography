package com.example.graduationproject.ui.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationproject.R;
import com.example.graduationproject.data.local.PublicKeyToStore;
import com.example.graduationproject.exception.MyException;
import com.example.graduationproject.ui.adapters.KeyAdapter;
import com.example.graduationproject.utils.AuthenticateFingerprint;
import com.example.graduationproject.utils.FileHelper;
import com.example.graduationproject.utils.KeyToStoreHelper;

import java.util.ArrayList;
import java.util.List;

public class KeyManagerFragment extends Fragment {
    private Button btnGenKeyPair, btnSign;
    private RecyclerView recyclerView;
    private List<PublicKeyToStore> keyList;
    private final String PUBLIC_FILE_NAME = "public.dat";


//     allow the fragment to fetch data and display
    public static KeyManagerFragment newInstance(List<PublicKeyToStore> keyList) {
        KeyManagerFragment fragment = new KeyManagerFragment();
        Bundle args = new Bundle();
        args.putSerializable("keyList", new ArrayList<>(keyList));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            keyList = (List<PublicKeyToStore>) getArguments().getSerializable("keyList");
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_key_manager, container, false);
        recyclerView = view.findViewById(R.id.key_recycler_view);
        btnGenKeyPair = view.findViewById(R.id.btnGenerateKey);
        btnSign = view.findViewById(R.id.btnSign);
        setupRecyclerView();
        setupGenerateKeyPairButton();
        return view;
    }

    private void setupRecyclerView(){
        if (keyList == null) {
            Toast.makeText(getContext(),"key list is null", Toast.LENGTH_SHORT).show();
            return;
        }
        KeyAdapter adapter = new KeyAdapter(getActivity(), keyList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    };
    private void setupGenerateKeyPairButton(){
        btnGenKeyPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticateFingerprint.authenticate(
                        getContext(),
                        () -> showGenKeyPopup(),
                        "Authenticate to generate key pair");
            }
        });
    };

    private void showGenKeyPopup() {
        if (getActivity() == null || !isAdded()) {
            Toast.makeText(getContext(),"cannot show popup", Toast.LENGTH_SHORT).show();
            return; // Fragment not attached, cannot show dialog
        }
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View popupView = inflater.inflate(R.layout.popup_genkey, null);

        // create the popup window
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(popupView);
        final AlertDialog dialog = builder.create();

        // initialize the elements of the popup
        EditText edText = popupView.findViewById(R.id.edTextPopup);
        Spinner spinner = popupView.findViewById(R.id.spinnerPopup);
        Button btnGenKey = popupView.findViewById(R.id.btnGenKeyPopup);
        List<String> paraTypes = new ArrayList<>();
        paraTypes.add("dilithium2");
        paraTypes.add("dilithium3");
        paraTypes.add("dilithium5");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, paraTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnGenKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyAlias = edText.getText().toString();
                String dilithiumType = spinner.getSelectedItem().toString();
                if (keyAlias.length() == 0) {
                    Toast.makeText(v.getContext(), "Please insert key alias!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("popup", keyAlias + dilithiumType);

                // gen dilithium key pair
                try {
                    KeyToStoreHelper.generateDilithiumKeyPair(keyAlias, v.getContext(), dilithiumType);
                } catch (MyException e) {
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(v.getContext(), "Generated dilithium keypair", Toast.LENGTH_SHORT).show();

                // update the recycler view
                updateRecyclerView();

                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void updateRecyclerView() {
        Log.d("KeyManagerFragment", "updateing recycler view: " + keyList.size());
        keyList = FileHelper.retrievePublicKeyFromFile(getActivity());
        KeyAdapter keyAdapter = (KeyAdapter) recyclerView.getAdapter();
        keyAdapter.updateKeyList(keyList);
    }
}
