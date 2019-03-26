package com.android.earBook.entity;

import javax.persistence.*;

/**
 * 用户实体
 * @author poshichao
 */
@Entity
public class User {
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 人脸图片路径
     */
    private String faceId;
}
