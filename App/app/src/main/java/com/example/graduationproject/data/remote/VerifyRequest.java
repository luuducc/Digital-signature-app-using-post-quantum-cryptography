package com.example.graduationproject.data.remote;

public class VerifyRequest {
    private String keyId;
    private String initialHashedMessage;
    private String signature;
    private String className;
    // if true => pdf signature, else json signature
    private boolean isPdfElseJson;
    public static boolean PDF_SIGNATURE = true;
    public static boolean JSON_SIGNATURE = false;

    public VerifyRequest(
            String className, String keyId, String initialHashedMessage, String signature, boolean isPdfElseJson
    ) {
        this.className = className;
        this.keyId = keyId;
        this.initialHashedMessage = initialHashedMessage;
        this.signature = signature;
        this.isPdfElseJson = isPdfElseJson;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean isPdfElseJson() {
        return isPdfElseJson;
    }

    public void setPdfElseJson(boolean pdfElseJson) {
        isPdfElseJson = pdfElseJson;
    }
}
