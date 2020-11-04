package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
消费者-控制层
 */
@RestController //@Controller+@ResponseBody
@RequestMapping("/user")
public class UserController {

    //@Autowired
    //负载均衡 轮询roundrobin
    @Reference(loadbalance = "roundrobin")
    private UserService userService;

    /**
     * 根据id查询用户信息
     */
    @RequestMapping("/findById")
    public User findById(Integer id){
        return userService.findById(id);
    }
}
