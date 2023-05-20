package com.bom.zcloudbackend.common.util;

import com.bom.zcloudbackend.config.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Date;

/**
 * @author Frank Liang
 */
@Component
public class JWTUtil {

    @Resource
    JwtProperties jwtProperties;


    /**
     * 由字符串生成密钥
     * @return  密钥
     */
    private SecretKey generalKey() {
        // 本地的密码解码
        byte[] encodedKey = Base64.decodeBase64(jwtProperties.getSecret());
        // 根据给定的字节数组使用AES加密算法构造一个密钥
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    public String createJWT(String subject) throws Exception {

        // 生成JWT的时间
        long nowTime = System.currentTimeMillis();
        Date nowDate = new Date(nowTime);
        // 生成签名的时候使用的秘钥secret
        SecretKey key = generalKey();

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine se = manager.getEngineByName("js");
        int expireTime = 0;
        try {
            expireTime =(int) se.eval(jwtProperties.getPayload().getRegisterdClaims().getExp());
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        // 为payload添加各种标准声明和私有声明
        DefaultClaims defaultClaims = new DefaultClaims();
        defaultClaims.setIssuer(jwtProperties.getPayload().getRegisterdClaims().getIss());
        defaultClaims.setExpiration(new Date(System.currentTimeMillis() + expireTime));
        defaultClaims.setSubject(subject);
        defaultClaims.setAudience(jwtProperties.getPayload().getRegisterdClaims().getAud());

        // 表示new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
            .setClaims(defaultClaims)
            .setIssuedAt(nowDate)
            // 设置签名，使用的是签名算法和签名使用的秘钥
            .signWith(SignatureAlgorithm.forName(jwtProperties.getHeader().getAlg()), key);
        return builder.compact();
    }

    /**
     * 解析JWT
     * @param jwt
     * @return
     * @throws Exception
     */
    public Claims parseJWT(String jwt) throws Exception {
        // 签名秘钥，和生成的签名的秘钥一模一样
        SecretKey key = generalKey();
        // 得到DefaultJwtParser
        Claims claims = Jwts.parser()
            // 设置签名的秘钥
            .setSigningKey(key)
            // 设置需要解析的jwt
            .parseClaimsJws(jwt).getBody();
        return claims;
    }


}
