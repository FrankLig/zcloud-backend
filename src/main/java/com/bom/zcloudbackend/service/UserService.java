package com.bom.zcloudbackend.service;

import com.bom.zcloudbackend.common.RespResult;
import com.bom.zcloudbackend.entity.User;

public interface UserService {

    RespResult<String> registerUser(User user);

    RespResult<User> login(User user);

}
