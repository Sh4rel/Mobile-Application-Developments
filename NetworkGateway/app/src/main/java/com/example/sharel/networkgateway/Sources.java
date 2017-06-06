package com.example.sharel.networkgateway;

import java.io.Serializable;

/**
 * Created by Sharel on 4/20/2017.
 */

public class Sources implements Serializable{

    String id;
    String source;
    String description;
    String url;
    String category;

    public Sources(String id, String source, String description, String url, String category) {
        this.id = id;
        this.source = source;
        this.description = description;
        this.url = url;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
