package com.android.earBook.service;

import com.android.earBook.entity.User;
import com.android.earBook.exception.EntityDuplicateException;
import com.android.earBook.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

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

    @Override
    public void login(User user) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        User existUser = userRepository.findByUsername(user.getUsername());

        if (existUser != null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else if (!user.getPassword().equals(existUser.getPassword())) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            response.setStatus(HttpStatus.OK.value());
        }
    }
}
