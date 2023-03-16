package com.bom.zcloudbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bom.zcloudbackend.common.RespResult;
import com.bom.zcloudbackend.common.util.DateUtil;
import com.bom.zcloudbackend.common.util.EncryptUserUtil;
import com.bom.zcloudbackend.common.util.JWTUtil;
import com.bom.zcloudbackend.entity.User;
import com.bom.zcloudbackend.mapper.UserMapper;
import com.bom.zcloudbackend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JWTUtil jwtUtil;

    @Resource
    private EncryptUserUtil encryptUserUtil;

    @Override
    public RespResult<String> registerUser(User user) {
//        String telephone = user.getTelephone();
        String password = user.getPassword();
        String username = user.getUsername();

//        if (!StringUtils.hasLength(telephone)) {
//            return RespResult.fail().message("手机号不能为空");
//        }
        if (!StringUtils.hasLength(password)) {
            return RespResult.fail().message("密码不能为为空");
        }
        if (!StringUtils.hasLength(username)) {
            return RespResult.fail().message("用户名不能为空");
        }
        if (isExistUsername(username)) {
            return RespResult.fail().message("用户名已存在");
        }
//        if (isExistPhone(telephone)) {
//            return RespResult.fail().message("该手机号已被注册");
//        }

        String salt = UUID.randomUUID().toString().replace("-", "").substring(15);
        String ps = password + salt;
        String newPassword = DigestUtils.md5DigestAsHex(ps.getBytes());

        //生成加密key并加密存储到数据库
        String userKey = UUID.randomUUID().toString();
        try {
            String aesEncrypt = EncryptUserUtil.aesEncrypt(userKey);
            user.setEncryptKey(aesEncrypt);
        } catch (Exception e) {
            return RespResult.fail().message("生成加密密钥失败");
        }

        user.setSalt(salt);
        user.setPassword(newPassword);
        user.setRegisterTime(DateUtil.getCurrentTime());

        int result = userMapper.insert(user);
        if (result == 1) {
            return RespResult.success();
        } else {
            return RespResult.fail().message("注册失败，请检查注册信息");
        }
    }

    private boolean isExistPhone(String telephone) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getTelephone, telephone);
        List<User> list = userMapper.selectList(lambdaQueryWrapper);
        if (list != null && !list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isExistUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        List<User> list = userMapper.selectList(wrapper);
        if (list != null && !list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public RespResult<User> login(User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User saveUser = userMapper.selectOne(wrapper);
        String salt = saveUser.getSalt();
        String ps = password + salt;
        //md5算法不可逆
        String newPassword = DigestUtils.md5DigestAsHex(ps.getBytes());

        if (newPassword.equals(saveUser.getPassword())) {
            saveUser.setPassword("");
            saveUser.setSalt("");
            return RespResult.success().data(saveUser);
        } else {
            return RespResult.fail().message("用户名或者密码错误");
        }
    }

    @Override
    public User getUserByToken(String token) {
        User tokenUserInfo = null;

        Claims claims = null;
        try {
            claims = jwtUtil.parseJWT(token);
            String subject = claims.getSubject();
            ObjectMapper objectMapper = new ObjectMapper();
            tokenUserInfo = objectMapper.readValue(subject, User.class);
        } catch (Exception e) {
            log.error("解码异常");
            return null;
        }
        return tokenUserInfo;

    }
}
