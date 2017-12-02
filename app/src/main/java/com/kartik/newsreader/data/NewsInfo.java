package com.kartik.newsreader.data;

import android.media.Image;

/**
 * Created by kartik on 7/11/17.
 */

public class NewsInfo {
    private String title;
    private String author;
    private String url;
    private String thumbNailURL;
    private String desc;
    private String source;
    public static final String AUTHOR_PREFIX = "by";
    public static final String SOURCE_PREFIX = "from";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbNailURL() {
        return thumbNailURL;
    }

    public void setThumbNailURL(String thumbNailURL) {
        this.thumbNailURL = thumbNailURL;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String toString() {
        return "\nTitle: " + title + "\nAuthor: " + author + "\nURL: " + url + "\nDescription: " + desc + "\nImageURL: " + thumbNailURL;
    }
}
