package com.davidtpate.speedtest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostParent {
    @SerializedName("data")
    @Expose
    private Post mPost;
    
    public PostParent(Post mPost) {
     this.mPost = mPost;   
    }

    public Post getPost() {
        return mPost;
    }

}
