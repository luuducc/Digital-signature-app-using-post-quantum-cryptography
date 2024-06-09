package com.example.graduationproject.data.remote;

public class VerifyResponse {
    private boolean result;

    public VerifyResponse(boolean result) {
        this.result = result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
