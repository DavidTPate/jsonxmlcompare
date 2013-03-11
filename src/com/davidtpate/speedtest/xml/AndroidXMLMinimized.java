package com.davidtpate.speedtest.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.davidtpate.jsonxml.R;

import android.util.Log;
import android.util.Xml;

public class AndroidXMLMinimized extends AndroidXML {

    public String getName() {
        return "Android-XML-Mini";
    }

    public int getResource() {
        return R.raw.reddit_android_xml_minimized;
    }
}
