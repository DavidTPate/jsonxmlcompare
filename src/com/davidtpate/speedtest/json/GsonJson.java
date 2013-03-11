package com.davidtpate.speedtest.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.davidtpate.jsonxml.R;
import com.davidtpate.speedtest.TestParser;
import com.davidtpate.speedtest.model.Post;
import com.davidtpate.speedtest.model.PostParent;
import com.davidtpate.speedtest.model.Post.Builder;
import com.google.gson.stream.JsonReader;

public class GsonJson implements TestParser {

    String                      mName       = null;
    private static Post.Builder postBuilder = new Post.Builder();

    public String getName() {
        return "Gson";
    }

    public int getResource() {
        return R.raw.reddit_android_json;
    }

    public String getUrl() {
        return "http://www.reddit.com/r/android.json";
    }

    public List<PostParent> parse(InputStream inputStream) {

        List<PostParent> result = null;

        JsonReader reader;
        try {
            reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            result = readFeed(reader);
            reader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private List<PostParent> readFeed(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            mName = reader.nextName();
            if (mName.equalsIgnoreCase("data")) {
                return readData(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return null;
    }

    private List<PostParent> readData(JsonReader reader) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            mName = reader.nextName();
            if (mName.equalsIgnoreCase("children")) {
                return readChildren(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return null;
    }

    private List<PostParent> readChildren(JsonReader reader) throws IOException {
        List<PostParent> result = new ArrayList<PostParent>();

        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                mName = reader.nextName();
                if (mName.equalsIgnoreCase("data")) {
                    result.add(new PostParent(readChildData(reader)));
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
        }
        reader.endArray();
        return result;
    }

    private Post readChildData(JsonReader reader) throws IOException {
        String subreddit = null;
        String title = null;
        String url = null;
        int upVotes = 0;
        int downVotes = 0;
        String thumbnail = null;

        reader.beginObject();
        while (reader.hasNext()) {
            mName = reader.nextName();
            if (mName.equalsIgnoreCase("subreddit"))
                subreddit = reader.nextString();
            else if (mName.equalsIgnoreCase("title"))
                title = reader.nextString();
            else if (mName.equalsIgnoreCase("url"))
                url = reader.nextString();
            else if (mName.equalsIgnoreCase("ups"))
                upVotes = reader.nextInt();
            else if (mName.equalsIgnoreCase("downs"))
                downVotes = reader.nextInt();
            else if (mName.equalsIgnoreCase("thumbnail"))
                thumbnail = reader.nextString();
            else
                reader.skipValue();
        }
        reader.endObject();
        return postBuilder.subreddit(subreddit).title(title).url(url).upvotes(upVotes).downvotes(downVotes).thumbnail(thumbnail).build();
    }
}
