package com.manoj.phonyhub.data;

import java.util.HashMap;
import java.util.Map;

public class PicsumDataModel {

    public String id;
    public String author;
    public String url;
    public String download_url;
    public int width, height;
    private Map<String, Object> extraProperties = new HashMap<String, Object>();

//    public PicsumDataModel(String id, String author, String url, String download_url, int width, int height) {
//        this.id = id;
//        this.author = author;
//        this.url = url;
//        this.download_url = download_url;
//        this.width = width;
//        this.height = height;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Map<String, Object> getExtraProperties() {
        return this.extraProperties;
    }

    public void setExtraProperty(String title, Object value) {
        this.extraProperties.put(title, value);
    }

}
