package com.liuzihan.admin.controller;

import com.liuzihan.framework.annotation.SystemLog;
import com.liuzihan.framework.domain.ResponseResult;
import com.liuzihan.framework.domain.entity.User;
import com.liuzihan.framework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PutMapping("/updateUserStatus")
    @SystemLog(businessName = "更新用户状态")
    public ResponseResult updateUserStatus(@RequestBody User user){
        return userService.updateUserStatus(user);
    }

    @PostMapping("/selectUser")
    @SystemLog(businessName = "筛选用户")
    public ResponseResult selectUser(@RequestBody User user,Integer pageNum, Integer pageSize){
        return userService.selectUser(user,pageNum,pageSize);
    }

    @DeleteMapping("/deleteUser/{id}")
    @SystemLog(businessName = "删除用户")
    public ResponseResult deleteUser(@PathVariable Long id){
        return userService.deleteUser(id);
    }
}
