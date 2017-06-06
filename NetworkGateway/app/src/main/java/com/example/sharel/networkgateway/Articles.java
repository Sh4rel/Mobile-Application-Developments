package com.example.sharel.networkgateway;

import java.io.Serializable;

/**
 * Created by Sharel on 4/21/2017.
 */

public class Articles  implements Serializable{
    String author;
    String title;
    String desc;
    String url;
    String urlToImage;
    String publishDate;

    public Articles(String author, String title, String desc, String url, String urlToImage, String publishDate) {

      this.author=author;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishDate = publishDate;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }
}
