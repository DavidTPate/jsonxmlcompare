package com.davidtpate.speedtest;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.davidtpate.speedtest.model.PostParent;

public interface TestParser {

    String getName();
    int getResource();
    String getUrl();
    List<PostParent> parse(InputStream inputStream);
}
