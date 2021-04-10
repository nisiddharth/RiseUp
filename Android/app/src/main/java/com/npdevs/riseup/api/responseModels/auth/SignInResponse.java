package com.npdevs.riseup.api.responseModels.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.npdevs.riseup.api.responseModels.Response;

public class SignInResponse extends Response {
    @SerializedName("token")
    @Expose
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
