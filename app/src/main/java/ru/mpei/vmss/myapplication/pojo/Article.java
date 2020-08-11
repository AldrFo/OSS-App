package ru.mpei.vmss.myapplication.pojo;

import android.media.Image;

public class Article {

    private String title;
    private String content;
    private String date;
    private Image img;
    private String imageUrl;

    public Article(String title, String content, String date, Image img, String imageUrl) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.img = img;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}