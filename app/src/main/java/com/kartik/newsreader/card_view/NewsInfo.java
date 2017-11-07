package com.kartik.newsreader.card_view;

/**
 * Created by kartik on 7/11/17.
 */

public class NewsInfo {
    private String title;
    private String author;
    private String url;
    protected static final String AUTHOR_PREFIX = "by ";

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

}
