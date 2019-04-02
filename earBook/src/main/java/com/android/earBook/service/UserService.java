package com.android.earBook.service;

import com.android.earBook.entity.User; /**
 * 用户service，进行用户相关逻辑操作
 * @author poshichao
 */
public interface UserService {
    void registered(User user);
}
