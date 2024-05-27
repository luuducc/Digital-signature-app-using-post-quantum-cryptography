package com.example.keystorelearning;

import java.util.UUID;

public class KeyToStore {
    private final UUID uuid;
    private final String keyAlias;
    private final String dilithiumParametersType;

    public KeyToStore(UUID uuid, String keyAlias, String dilithiumParametersType) {
        this.uuid = uuid;
        this.keyAlias = keyAlias;
        this.dilithiumParametersType = dilithiumParametersType;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getKeyAlias() {
        return keyAlias;
    }

    public String getDilithiumParametersType() {
        return dilithiumParametersType;
    }
}
