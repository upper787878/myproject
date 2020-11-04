package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.UserDao;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@Service//(protocol = "dubbo")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findById(Integer userId) {
        System.out.println("dubbo_provider2..................");
        /*try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return userDao.findById(userId);
    }
}
