package com.itheima.service;

import com.itheima.pojo.User;

/**
 * 服务接口（消费者 服务提供者使用）
 */
public interface UserService {

    /**
     * 根据用户id查询用户对象
     */
    public User findById(Integer userId);
}
