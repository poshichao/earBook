package com.android.earBook.repository;

import com.android.earBook.entity.User;
import org.springframework.data.repository.CrudRepository;

/**
 * 用户仓库，用于数据库操作
 * @author poshichao
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
