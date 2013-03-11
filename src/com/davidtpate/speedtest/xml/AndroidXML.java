package com.davidtpate.speedtest.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.davidtpate.jsonxml.R;
import com.davidtpate.speedtest.TestParser;
import com.davidtpate.speedtest.model.Post;
import com.davidtpate.speedtest.model.PostParent;

public class AndroidXML implements TestParser {

    Post.Builder postBuilder = new Post.Builder();
    String       ns          = null;

    public String getName() {
        return "Android-XML";
    }

    public int getResource() {
        return R.raw.reddit_android_xml;
    }

    public String getUrl() {
        return "http://www.reddit.com/r/android.xml";
    }

    public List<PostParent> parse(InputStream inputStream) {

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();

            return readRss(parser);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private List<PostParent> readRss(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<PostParent> posts = new ArrayList<PostParent>();

        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            if (name.equals("channel")) {
                parser.require(XmlPullParser.START_TAG, ns, "channel");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    name = parser.getName();
                    if (name.equals("item")) {
                        posts.add(readItem(parser));
                    } else {
                        skip(parser);
                    }
                }
            } else {
                skip(parser);
            }

        }
        return posts;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private PostParent readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");

        String subreddit = null;
        String title = null;
        String url = null;
        int upVotes = 0;
        int downVotes = 0;
        String thumbnail = null;
        String temp = null;
        String name = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("link")) {
                url = readLink(parser);
            } else if (name.equals("media:thumbnail")) {
                thumbnail = readThumbnail(parser);
            } else if (name.equals("media:title")) {
                subreddit = readSubreddit(parser);
            } else if (name.equals("guid")) {
                temp = readUpVotes(parser);
                upVotes = 0;
            } else if (name.equals("pubDate")) {
                temp = readDownVotes(parser);
                downVotes = 0;
            } else {
                skip(parser);
            }
        }
        return new PostParent(postBuilder.subreddit(subreddit == null ? " " : subreddit).title(title).url(url).upvotes(upVotes).downvotes(downVotes).thumbnail(thumbnail == null ? " " : thumbnail).build());
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return title;
    }

    private String readThumbnail(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, "media:thumbnail");
        String tag = parser.getName();
        if (tag.equals("media:thumbnail")) {
            link = parser.getAttributeValue(null, "url");
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, ns, "media:thumbnail");
        return link;
    }

    private String readSubreddit(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "media:title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "media:title");
        return title;
    }

    private String readUpVotes(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "guid");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "guid");
        return title;
    }

    private String readDownVotes(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
