package com.bom.zcloudbackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bom.zcloudbackend.common.RespResult;
import com.bom.zcloudbackend.entity.User;

/**
 * @author Frank Liang
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param user
     * @return
     */
    RespResult<String> registerUser(User user);

    /**
     * 用户登录
     * @param user
     * @return
     */
    RespResult<User> login(User user);

    /**
     * 通过token获取用户信息
     * @param token
     * @return
     */
    User getUserByToken(String token);
}
