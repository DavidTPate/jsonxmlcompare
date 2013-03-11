package com.davidtpate.speedtest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataModel {
    @SerializedName("data")
    @Expose
    private Posts mPosts;

    public Posts getPosts() {
        return mPosts;
    }
    
}
