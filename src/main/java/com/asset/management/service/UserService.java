package com.asset.management.service;

import com.asset.management.dao.UserDao;
import com.asset.management.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public Page<User> listAll(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page,size);
        return userDao.findAll(pageable);
    }

    public User findByName(String name){
        return userDao.findByName(name);
    }

    public User findById(int id){
        return userDao.findById(id).get();
    }

    public User addUser(User user){
        return userDao.save(user);
    }

    public User updateUser(User user){
        return userDao.save(user);
    }
    public void deleteUser(int id){
        userDao.deleteById(id);
    }

    /**
     * 批量删除
     * @param ids 删除对象id组成的对象组
     * @return
     */
    public String bathDeleteUser(String ids){
        List idList = Arrays.asList(ids.split(","));
        idList.forEach(id ->{
            Integer userId = Integer.parseInt((String) id);
            userDao.deleteById(userId);
        });
        return "success";
    }
}
