package com.bom.zcloudbackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bom.zcloudbackend.common.RespResult;
import com.bom.zcloudbackend.entity.User;

public interface UserService extends IService<User> {

    RespResult<String> registerUser(User user);

    RespResult<User> login(User user);

    User getUserByToken(String token);
}
