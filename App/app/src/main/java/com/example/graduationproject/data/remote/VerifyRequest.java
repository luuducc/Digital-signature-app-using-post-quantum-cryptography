package com.example.graduationproject.data.remote;

public class VerifyRequest {
    private String keyId;
    private String initialHashedMessage;
    private String signature;

    public VerifyRequest(String keyId, String initialHashedMessage, String signature) {
        this.keyId = keyId;
        this.initialHashedMessage = initialHashedMessage;
        this.signature = signature;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getInitialHashedMessage() {
        return initialHashedMessage;
    }

    public void setInitialHashedMessage(String initialHashedMessage) {
        this.initialHashedMessage = initialHashedMessage;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
