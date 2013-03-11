package com.davidtpate.speedtest.model;

import com.davidtpate.util.Log;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("subreddit")
    @Expose
    private String mSubreddit;
    @SerializedName("title")
    @Expose
    private String mTitle;
    @SerializedName("url")
    @Expose
    private String mUrl;
    @SerializedName("ups")
    @Expose
    private int    mUpVotes   = -3333333;
    @SerializedName("downs")
    @Expose
    private int    mDownVotes = -3333333;
    @SerializedName("thumbnail")
    @Expose
    private String mThumbnail;

    public String getSubreddit() {
        return mSubreddit;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public int getUpVotes() {
        return mUpVotes;
    }

    public int getDownVotes() {
        return mDownVotes;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public static class Builder {
        private Post mPost = new Post();

        public Builder subreddit(String mSubreddit) {
            this.mPost.mSubreddit = mSubreddit;
            return this;
        }

        public Builder title(String mTitle) {
            this.mPost.mTitle = mTitle;
            return this;
        }

        public Builder url(String mUrl) {
            this.mPost.mUrl = mUrl;
            return this;
        }

        public Builder upvotes(int mUpVotes) {
            this.mPost.mUpVotes = mUpVotes;
            return this;
        }

        public Builder downvotes(int mDownVotes) {
            this.mPost.mDownVotes = mDownVotes;
            return this;
        }

        public Builder thumbnail(String mThumbnail) {
            this.mPost.mThumbnail = mThumbnail;
            return this;
        }

        public Post build() {
            Post post = this.mPost;
            this.mPost = new Post();

            return post;
        }
    }
}
