package com.davidtpate.speedtest.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.davidtpate.jsonxml.R;
import com.davidtpate.speedtest.TestParser;
import com.davidtpate.speedtest.model.PostParent;
import com.davidtpate.speedtest.model.Posts;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

public class GsonJsonAuto implements TestParser {

    public String getName() {
        return "Gson-Auto";
    }

    public int getResource() {
        return R.raw.reddit_android_json;
    }

    public String getUrl() {
        return "http://www.reddit.com/r/android.json";
    }

    public List<PostParent> parse(InputStream inputStream) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        JsonParser parser = new JsonParser();
        JsonObject obj = null;
        try {
            obj = parser.parse(new JsonReader(new InputStreamReader(inputStream, "UTF-8"))).getAsJsonObject();
        } catch (JsonIOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Posts posts = gson.fromJson(obj.get("children"), Posts.class);

        return posts.getPosts();
    }
}
