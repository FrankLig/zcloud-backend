package com.bom.zcloudbackend;

import com.bom.zcloudbackend.common.util.EncryptUserUtil;
import com.bom.zcloudbackend.config.jwt.JwtProperties;
import com.bom.zcloudbackend.entity.User;
import com.bom.zcloudbackend.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class ZcloudBackendApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private EncryptUserUtil encryptUserUtil;


    @Test
    void contextLoads() {
    }

    @Test
    public void test01() {
        User user = new User();
        user.setUsername("frank");
        user.setPassword("password");
        user.setTelephone("telephone");
        userMapper.insert(user);
        List<User> users = userMapper.selectList(null);
        System.out.println("查询结果:");
        users.forEach(System.out::println);
    }




}
