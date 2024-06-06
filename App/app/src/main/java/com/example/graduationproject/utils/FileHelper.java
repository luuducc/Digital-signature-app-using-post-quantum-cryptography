package com.example.graduationproject.utils;

import android.content.Context;
import android.util.Log;

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
    public static <T extends KeyToStore> void writeJsonKeyToFile(T key, Context context, String fileName) throws MyException {
        List<T> keyList = retrieveKeyFromFile(context, fileName, TypeToken.getParameterized(List.class, key.getClass()).getType());

        boolean isPrivate = key instanceof PrivateKeyToStore;

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
    public static List<PrivateKeyToStore> retrievePrivateKeyFromFile(Context context, String fileName) {
        return retrieveKeyFromFile(context, fileName, new TypeToken<List<PrivateKeyToStore>>() {}.getType());
    }
    public static List<PublicKeyToStore> retrievePublicKeyFromFile(Context context, String fileName) {
        return retrieveKeyFromFile(context, fileName, new TypeToken<List<PublicKeyToStore>>() {}.getType());
    }
}
