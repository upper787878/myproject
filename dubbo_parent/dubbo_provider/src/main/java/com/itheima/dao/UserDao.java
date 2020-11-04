package com.itheima.dao;

import com.itheima.pojo.User;

public interface UserDao {
    /**
     * 根据用户id查询用户对象
     */
    public User findById(Integer userId);
}
