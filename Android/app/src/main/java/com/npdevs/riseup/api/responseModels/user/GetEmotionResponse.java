package com.npdevs.riseup.api.responseModels.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.npdevs.riseup.api.responseModels.Response;

import java.util.List;

public class GetEmotionResponse extends Response {
    @SerializedName("data")
    @Expose
    private List<List<String>> data;

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }
}
