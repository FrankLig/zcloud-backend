package com.bom.zcloudbackend.controller;

import com.bom.zcloudbackend.common.RespResult;
import com.bom.zcloudbackend.common.util.JWTUtil;
import com.bom.zcloudbackend.dto.RegisterDTO;
import com.bom.zcloudbackend.entity.User;
import com.bom.zcloudbackend.service.UserService;
import com.bom.zcloudbackend.vo.LoginVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author Frank Liang
 */
@Api(tags = "用户")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    JWTUtil jwtUtil;

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public RespResult<String> register(@RequestBody RegisterDTO registerDTO) {
        RespResult<String> result = null;
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(registerDTO.getPassword());
        user.setTelephone(registerDTO.getTelephone());

        result = userService.registerUser(user);
        return result;
    }

    @ApiOperation(value = "用户登录", notes = "用户登录后进入系统")
    @GetMapping("/login")
    public RespResult<LoginVO> login(String username, String password) {

        LoginVO loginVO = new LoginVO();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        RespResult<User> loginResult = userService.login(user);

        if (!loginResult.getSuccess()) {
            return RespResult.fail().message("登录失败");
        }

        loginVO.setUsername(loginResult.getData().getUsername());
        String jwt = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            jwt = jwtUtil.createJWT(mapper.writeValueAsString(loginResult.getData()));
        } catch (Exception e) {
            return RespResult.fail().message("登录失败");
        }
        loginVO.setToken(jwt);
        return RespResult.success().data(loginVO);
    }

    @ApiOperation(value = "登录校验", notes = "校验token有效性")
    @GetMapping("/checkUserLoginInfo")
    public RespResult<User> checkToken(@RequestHeader(value = "token", required = false) String token) {
        if (token == null) {
            return RespResult.fail().message("用户暂未登录");
        }

        User tokenUserInfo = null;
        try {

            Claims c = jwtUtil.parseJWT(token);
            String subject = c.getSubject();
            ObjectMapper objectMapper = new ObjectMapper();
            tokenUserInfo = objectMapper.readValue(subject, User.class);

        } catch (Exception e) {
            log.error("解码异常");
            return RespResult.fail().message("认证失败");

        }

        if (tokenUserInfo != null) {

            return RespResult.success().data(tokenUserInfo);

        } else {
            return RespResult.fail().message("用户暂未登录");
        }
    }

}
