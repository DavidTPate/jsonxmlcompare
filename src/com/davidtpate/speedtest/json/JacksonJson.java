package com.davidtpate.speedtest.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.davidtpate.jsonxml.R;
import com.davidtpate.speedtest.TestParser;
import com.davidtpate.speedtest.model.Post;
import com.davidtpate.speedtest.model.PostParent;
import com.davidtpate.speedtest.model.Post.Builder;

import android.util.Log;

public class JacksonJson implements TestParser {

    private static JsonFactory  sJsonFactory = new JsonFactory();
    private static Post.Builder postBuilder  = new Post.Builder();
    String                      mName;

    public String getName() {
        return "Jackson";
    }

    public int getResource() {
        return R.raw.reddit_android_json;
    }

    public String getUrl() {
        return "http://www.reddit.com/r/android.json";
    }

    public List<PostParent> parse(InputStream inputStream) {

        List<PostParent> result = null;

        try {
            JsonParser parser = sJsonFactory.createJsonParser(inputStream);
            result = parseFeed(parser);
            parser.close();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<PostParent> parseFeed(JsonParser parser) throws JsonParseException, IOException {
        parser.nextToken();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            mName = parser.getCurrentName();
            if (mName.equalsIgnoreCase("data")) {
                return parseData(parser);
            } else {
                parser.nextToken();
            }
        }
        return null;
    }

    private List<PostParent> parseData(JsonParser parser) throws JsonParseException, IOException {
        parser.nextToken();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            mName = parser.getCurrentName();
            if (mName.equalsIgnoreCase("children")) {
                return parseChildren(parser);
            } else {
                parser.nextToken();
            }
        }
        return null;
    }

    private List<PostParent> parseChildren(JsonParser parser) throws JsonParseException, IOException {
        List<PostParent> result = new ArrayList<PostParent>();
        Post post;

        while (parser.nextToken() != JsonToken.END_ARRAY) {
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                mName = parser.getCurrentName();
                if (mName != null && mName.equalsIgnoreCase("data")) {

                    post = parseChildData(parser);
                    if (post != null)
                        result.add(new PostParent(post));
                } else {
                    parser.nextToken();
                }
            }
        }

        return result;
    }

    private Post parseChildData(JsonParser parser) throws JsonParseException, IOException {
        String subreddit = null;
        String title = null;
        String url = null;
        int upVotes = 0;
        int downVotes = 0;
        String thumbnail = null;

        parser.nextToken();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            mName = parser.getCurrentName();
            if (mName.equalsIgnoreCase("subreddit")) {
                parser.nextToken();
                subreddit = parser.getText();
            } else if (mName.equalsIgnoreCase("title")) {
                parser.nextToken();
                title = parser.getText();
            } else if (mName.equalsIgnoreCase("url")) {
                parser.nextToken();
                url = parser.getText();
            } else if (mName.equalsIgnoreCase("ups")) {
                parser.nextToken();
                upVotes = parser.getIntValue();
            } else if (mName.equalsIgnoreCase("downs")) {
                parser.nextToken();
                downVotes = parser.getIntValue();
            } else if (mName.equalsIgnoreCase("thumbnail")) {
                parser.nextToken();
                thumbnail = parser.getText();
            } else {
                parser.nextToken();
            }
        }
        if (parser.getCurrentToken() != JsonToken.END_OBJECT)
            return postBuilder.subreddit(subreddit).title(title).url(url).upvotes(upVotes).downvotes(downVotes).thumbnail(thumbnail).build();
        return null;
    }

}
