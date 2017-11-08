package com.kartik.newsreader.api;

import android.media.Image;

/**
 * Created by kartik on 7/11/17.
 */

public class NewsInfo {
    private String title;
    private String author;
    private String url;
    private String thumbNailURL;
    public static final String AUTHOR_PREFIX = "by ";

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
}
