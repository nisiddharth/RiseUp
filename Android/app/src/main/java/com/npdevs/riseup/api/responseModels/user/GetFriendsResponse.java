package com.npdevs.riseup.api.responseModels.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.npdevs.riseup.api.responseModels.Response;
import com.npdevs.riseup.datamodels.UserMeta;

import java.util.List;

public class GetFriendsResponse extends Response {
    @SerializedName("data")
    @Expose
    private List<UserMeta> friends;

    public List<UserMeta> getFriends() {
        return friends;
    }

    public void setFriends(List<UserMeta> friends) {
        this.friends = friends;
    }
}
