package com.akiwan.elasticsearch.controller;

import com.akiwan.elasticsearch.model.User;
import com.akiwan.elasticsearch.dao.UserDao;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping("/add")
    public User addUser(@RequestBody User user) throws Exception{
        return userDao.addUser(user);
    }

    @GetMapping("/view/all")
    public List<User> getAll(){
        return userDao.getAll();
    }

    @GetMapping("/view/{id}")
    public Map<String, Object> getUserById(@PathVariable String id){
        return userDao.getUserById(id);
    }

    @PutMapping("/update/{id}")
    public Map<String, Object> updateUserById(@RequestBody User user, @PathVariable String id){
        return userDao.updateUserById(id, user);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUserById(@PathVariable String id){
         userDao.deleteUserById(id);
    }
}
