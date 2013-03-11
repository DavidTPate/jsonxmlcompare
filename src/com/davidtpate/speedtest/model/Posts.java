package com.davidtpate.speedtest.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Posts {

    @SerializedName("children")
    @Expose
    private List<PostParent> mPosts;

    public List<PostParent> getPosts() {
        return mPosts;
    }

}
