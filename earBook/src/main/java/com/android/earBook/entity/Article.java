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
}
