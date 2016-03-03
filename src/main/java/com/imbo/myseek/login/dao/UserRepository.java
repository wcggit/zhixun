package com.imbo.myseek.login.dao;

import com.imbo.myseek.login.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wcg on 16/3/2.
 */
public interface UserRepository extends JpaRepository<User,Long>{

}
