package com.uca.entity;

import java.sql.Timestamp;

public class CommentEntity {
    private int id;
    private String content;
    private String author;
    private Timestamp created_time;
    private int article;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Timestamp getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Timestamp created_time) {
        this.created_time = created_time;
    }

    public int getArticle() {
        return article;
    }

    public void setArticle(int article) {
        this.article = article;
    }
}
