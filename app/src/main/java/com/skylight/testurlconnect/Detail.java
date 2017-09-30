package com.skylight.testurlconnect;

/**
 * Created by tangfen on 2017/9/28.
 *
 * @创建者 tangfen
 * @创建时间 2017/9/28 10:35
 * @描述 ${TODO}
 */

public class Detail {
    private String title;
    private String author;
    private String content;

    public Detail(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
