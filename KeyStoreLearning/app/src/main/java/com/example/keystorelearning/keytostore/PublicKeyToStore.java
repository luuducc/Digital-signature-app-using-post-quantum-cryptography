package com.example.keystorelearning.keytostore;

import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

public class PublicKeyToStore extends KeyToStore {
    private final String publicKey;
    private boolean isRegistered = false;
    private boolean visibility = false;
//    private UUID uuid;

    public PublicKeyToStore(byte[] publicKeyByte, UUID uuid, String keyAlias, String dilithiumParametersType) {
        super(uuid, keyAlias, dilithiumParametersType);
        this.publicKey = Base64.getEncoder().encodeToString(publicKeyByte);
    }

    public byte[] getPublicKey() {
        return Base64.getDecoder().decode(publicKey);
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        PublicKeyToStore other = (PublicKeyToStore) obj;
        UUID otherUUID = other.getUuid();
        byte[] otherPublicKey = other.getPublicKey();

        boolean result1 = otherUUID.equals(this.getUuid());
        boolean result2 = Arrays.equals(otherPublicKey, this.getPublicKey());

        return result1 && result2;
    }
}
