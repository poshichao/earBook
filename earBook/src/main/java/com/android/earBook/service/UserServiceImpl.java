package com.android.earBook.service;

import com.android.earBook.entity.User;
import com.android.earBook.exception.EntityDuplicateException;
import com.android.earBook.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户service 实现类
 * @author poshichao
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    /**
     * 日志，用于写注释
     */
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class.getName());

    @Override
    public void registered(User user) {
        User existUser = userRepository.findByUsername(user.getUsername());
        if (existUser != null) {
            throw new EntityDuplicateException("该用户名已被使用");
        }

        userRepository.save(user);
    }
}
