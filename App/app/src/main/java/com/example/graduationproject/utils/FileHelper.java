package com.example.graduationproject.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.graduationproject.config.MyConstant;
import com.example.graduationproject.data.local.KeyToStore;
import com.example.graduationproject.data.local.PrivateKeyToStore;
import com.example.graduationproject.data.local.PublicKeyToStore;
import com.example.graduationproject.exception.MyException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    private static final String PRIVATE_FILE_NAME = MyConstant.PRIVATE_FILE_NAME;
    private static final String PUBLIC_FILE_NAME = MyConstant.PUBLIC_FILE_NAME;
    public static <T extends KeyToStore> void writeJsonKeyToFile(T key, Context context) throws MyException {
        boolean isPrivate = key instanceof PrivateKeyToStore;
        final String fileName;
        if (isPrivate) {
            fileName = PRIVATE_FILE_NAME;
        } else {
            fileName = PUBLIC_FILE_NAME;
        }
        List<T> keyList = retrieveKeyFromFile(context, fileName, TypeToken.getParameterized(List.class, key.getClass()).getType());


        File file = new File(context.getFilesDir(), fileName);

        // if file is empty, insert the first key
        if (keyList == null) {
            keyList = new ArrayList<>();
            keyList.add(key);

            try {
                FileOutputStream outputStream = new FileOutputStream(file, false);
                OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting().create();
                String jsonKeyString =  gson.toJson(keyList);
                writer.write(jsonKeyString);
                writer.write("\n");
                writer.close();
                outputStream.flush();
                outputStream.close();
                if (isPrivate) {
                    Log.d("File Helper", "added first private key to list: " + key.getKeyAlias());
                } else {
                    Log.d("File Helper", "added first public key to list: " + key.getKeyAlias());
                }
            } catch (Exception e) {
                throw new RuntimeException("failed to write key to file");
            }
            return;
        } else { // check for existed key or existed alias
            boolean existedAlias = checkExistedAlias(key.getKeyAlias(), keyList);
            if (existedAlias) { // check for existed alias
                if (isPrivate) {
                    Log.d("File Helper", "alias existed, skipping write operation for private key: " + key.getKeyAlias());
                } else {
                    Log.d("File Helper", "alias existed, skipping write operation for public key: " + key.getKeyAlias());
                }
                throw new MyException("Alias existed, please type new alias");
            }
            if (keyList.contains(key)) { // check for existed key
                if (isPrivate) {
                    Log.d("File Helper", "private key existed in list, do nothing: " + key.getKeyAlias());
                } else {
                    Log.d("File Helper", "public key existed in list, do nothing: " + key.getKeyAlias());
                }
                return;
            }

            // write new key to file
            keyList.add(key);
            try {
                if (file.exists()) {
                    file.delete();
                }
                FileOutputStream outputStream = new FileOutputStream(file, false);
                OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting().create();
                String jsonKeyString =  gson.toJson(keyList);
                writer.write(jsonKeyString);
                writer.write("\n");
                writer.close();
                outputStream.flush();
                outputStream.close();

                if (isPrivate) {
                    Log.d("File Helper", "added new private key to list: " + key.getKeyAlias());
                } else {
                    Log.d("File Helper", "added new public key to list: " + key.getKeyAlias());
                }
            } catch (Exception e) {
                throw new RuntimeException("failed to write key to file");
            }
        }
    }
    private static <T extends KeyToStore> boolean checkExistedAlias(String keyAlias, List<T> list) {
        List<String> aliasList = new ArrayList<>();
        for (T element : list) {
            if (element.getKeyAlias().equals(keyAlias)) {
                return true;
            }
        }
        return false;
    }
    private static <T extends KeyToStore> List<T>  retrieveKeyFromFile(Context context, String fileName, Type type) {
        Gson gson = new Gson();
        File file = new File(context.getFilesDir(), fileName);

        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(inputStream);
            StringBuilder stringBuilder = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
            reader.close();
            inputStream.close();
            String jsonString = stringBuilder.toString();

            return gson.fromJson(jsonString, type); // Use classType directly for deserialization
        } catch (Exception e) {
            throw new RuntimeException("failed to retrieve key",e);
        }
    }
    public static List<PrivateKeyToStore> retrievePrivateKeyFromFile(Context context) {
        return retrieveKeyFromFile(context, PRIVATE_FILE_NAME, new TypeToken<List<PrivateKeyToStore>>() {}.getType());
    }
    public static List<PublicKeyToStore> retrievePublicKeyFromFile(Context context) {
        return retrieveKeyFromFile(context, PUBLIC_FILE_NAME, new TypeToken<List<PublicKeyToStore>>() {}.getType());
    }

    public static void updateIsRegisteredField(PublicKeyToStore updatedKey, Context context) {
        List<PublicKeyToStore> retrievedPublicKeys = retrievePublicKeyFromFile(context);
        File file = new File(context.getFilesDir(), PUBLIC_FILE_NAME);
        boolean isUpdated = true;

        // update key list in keystore
        for (PublicKeyToStore key : retrievedPublicKeys) {
            if (updatedKey.getUuid().equals(key.getUuid())) {
                key.setRegistered(updatedKey.isRegistered());
                break;
            }
        }
        if (file.exists()) {
            file.delete();
        }

        // write again the updated key list to file
        try {
            FileOutputStream outputStream = new FileOutputStream(file, false);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting().create();
            String jsonKeyString = gson.toJson(retrievedPublicKeys);
            writer.write(jsonKeyString);
            writer.write("\n");
            writer.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e){
            throw new RuntimeException("failed to update registered field");
        }

    }
}
