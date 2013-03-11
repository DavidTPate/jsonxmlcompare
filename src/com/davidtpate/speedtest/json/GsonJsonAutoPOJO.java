package com.davidtpate.speedtest.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.davidtpate.jsonxml.R;
import com.davidtpate.speedtest.TestParser;
import com.davidtpate.speedtest.model.DataModel;
import com.davidtpate.speedtest.model.PostParent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class GsonJsonAutoPOJO implements TestParser {

    public String getName() {
        return "Gson-Auto-POJO";
    }

    public int getResource() {
        return R.raw.reddit_android_json;
    }

    public String getUrl() {
        return "http://www.reddit.com/r/android.json";
    }

    public List<PostParent> parse(InputStream inputStream) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        DataModel dm = gson.fromJson(reader, DataModel.class);

        return dm.getPosts().getPosts();
    }
}
