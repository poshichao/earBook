package com.android.earBook.entity;

import javax.persistence.*;

/**
 * 文章实体
 * @author poshichao
 */
@Entity
public class Article {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
