package com.example.graduationproject.data.remote;


public class RegisterKeyRequest {
    private String _id;
    private String dilithiumParametersType;
    private String publicKeyString;

    public RegisterKeyRequest(String _id, String dilithiumParametersType, String publicKeyString) {
        this._id = _id;
        this.dilithiumParametersType = dilithiumParametersType;
        this.publicKeyString = publicKeyString;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDilithiumParametersType() {
        return dilithiumParametersType;
    }

    public void setDilithiumParametersType(String dilithiumParametersType) {
        this.dilithiumParametersType = dilithiumParametersType;
    }

    public String getPublicKeyString() {
        return publicKeyString;
    }

    public void setPublicKeyString(String publicKeyString) {
        this.publicKeyString = publicKeyString;
    }
}
