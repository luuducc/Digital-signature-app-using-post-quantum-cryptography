package com.example.graduationproject.data.remote;

public class VerifyResponse {
    private boolean result;

    public VerifyResponse(boolean result) {
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
