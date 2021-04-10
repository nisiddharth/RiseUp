package com.npdevs.riseup.api.responseModels.videos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.npdevs.riseup.api.responseModels.Response;
import com.npdevs.riseup.datamodels.YoutubeVideo;

import java.util.List;

public class GetVideoResponse extends Response {
    @SerializedName("data")
    @Expose
    List<YoutubeVideo> videos;

    public List<YoutubeVideo> getVideos() {
        return videos;
    }

    public void setVideos(List<YoutubeVideo> videos) {
        this.videos = videos;
    }
}
