package com.example.keystorelearning;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

public class PrivateKeyToStore extends KeyToStore {
    private final String encryptedPrivateKey;
//    private UUID uuid;

    public PrivateKeyToStore(byte[] encryptedPrivateKey, UUID uuid, String keyAlias, String dilithiumParametersType) {
        super(uuid, keyAlias, dilithiumParametersType);
        this.encryptedPrivateKey = Base64.getEncoder().encodeToString(encryptedPrivateKey);
//        this.uuid = uuid;
    }

    public byte[] getEncryptedPrivateKey() {
        return Base64.getDecoder().decode(encryptedPrivateKey);
    }

//    public UUID getUuid() {
//        return uuid;
//    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        PrivateKeyToStore other = (PrivateKeyToStore) obj;
        UUID otherUUID = other.getUuid();

        byte[] otherEncryptedPrivateKey = other.getEncryptedPrivateKey();
        boolean result1 = otherUUID.equals(this.getUuid());
        boolean result2 = Arrays.equals(otherEncryptedPrivateKey, this.getEncryptedPrivateKey());

        return result1 && result2;
    }
}
