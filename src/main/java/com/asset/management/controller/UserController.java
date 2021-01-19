package com.asset.management.controller;

import com.asset.management.entity.ResultSet;
import com.asset.management.entity.User;
import com.asset.management.entity.UserBo;
import com.asset.management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 分页显示所有用户信息
     * @param page 第几页
     * @param size 每页数据行数
     * @return
     */
    @RequestMapping("/listAll/{page}/{size}")
    public Page<User> listAll(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
        return userService.listAll(page-1, size);
    }

    @RequestMapping("/login")
    public ResultSet login(@RequestBody UserBo bo, HttpServletRequest request){
        // 登录密码
        String password = bo.getPassword();
        // 登录手机号
        String name = bo.getName();
        User user = userService.findByName(name);
        ResultSet resultSet = new ResultSet();
        if(user == null || password==null){
            resultSet.setSuccess(false);
            return resultSet;
        }else if(!user.getPassword().equals(password)){
            resultSet.setSuccess(false);
            return resultSet;
        }
//        String token = tokenService.getToken(user);
        resultSet.setSuccess(true);
        return resultSet;
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @RequestMapping("/register")
    public String register(@RequestBody User user){
        User result =  userService.addUser(user);
        if(result != null){
            return "success";
        }else {
            return "error";
        }
    }

    @RequestMapping("/findById")
    public User findById(@RequestParam("id") Integer id){
        return userService.findById(id);
    }

    @RequestMapping("/findByName")
    public User findByName(@RequestParam("name") String name){
        User user =  userService.findByName(name);
        return user;
    }

    /**
     * 更新用户信息，id不可变
     * @param user
     * @return
     */
    @RequestMapping("/updateUser")
    public String updateUser(@RequestBody User user){
        User exist = findByName(user.getName());
        if(exist != null){
            User result =  userService.updateUser(user);
            if(result != null){
                return "success";
            }else {
                return "error";
            }
        }else {
            return "error";
        }

    }

    /**
     * 根据id删除用户
     * @param id
     */
    @RequestMapping("/deleteUser")
    public void deleteUser(@RequestParam("id")Integer id){
        userService.deleteUser(id);
    }

    /**
     * 批量删除
     * @param userIds
     */
    @RequestMapping("/bathDeleteUser")
    public void bathDeleteUser(@RequestParam("userIds") String userIds){
        userService.bathDeleteUser(userIds);
    }
}
