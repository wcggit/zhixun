package com.imbo.myseek.login.service;

import com.imbo.myseek.login.entities.User;
import com.imbo.myseek.login.dao.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by wcg on 16/3/2.
 */
@Service
public class UserService {
    @Inject
    private UserRepository userDao;

    public List<User> findAllUser(){
        return userDao.findAll();
    }

}
