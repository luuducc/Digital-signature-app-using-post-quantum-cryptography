package com.example.graduationproject.config;

import android.os.Environment;

public class MyConstant {
    public static final String PUBLIC_FILE_NAME = "public.dat";
    public static final String PRIVATE_FILE_NAME = "private.dat";
    public static final String SHARED_PREFERENCES_NAME = "graduation_preferences";
    public static final String KEY_STORE_KEY_ALIAS = "graduation_rsa_key";
    public static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    public static final String EXTRACTED_PRIVATE_KEY_FILE_NAME = "extracted_private_key.txt";
    public static final String DOMAIN = "http://192.168.1.196:5000";
    public static final String GRADUATION_PROJECT_FOLDER = Environment.getExternalStorageDirectory().toString() + "/Graduation Project";

}
