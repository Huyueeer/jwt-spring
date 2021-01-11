package com.huyue.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.huyue.bean.User;
import com.huyue.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class AdminUserController {

    @PostMapping(value = "/user/login")
    public Map<String, String> userLogin(@RequestBody User user) {
        log.info(user.getUsername());
        log.info(user.getPassword());

        // 数据库中查询用户信息
        User one = user;

        HashMap<String, String> result = new HashMap<>(); // 返回结果信息给前端

        if (one == null){
            result.put("code","401");
            result.put("msg","用户名或密码错误");
            return result;
        }

        Map<String, String> map = new HashMap<>(); //用来存放payload信息

        map.put("username",one.getUsername());

        // 生成token令牌
        String token = JWTUtil.generateToken(map);

        // 返回前端token
        result.put("code","200");
        result.put("msg","登录成功");
        result.put("token",token);
        return result;
    }

    @GetMapping(value = "/user/test")
    public Map<String,String> test(HttpServletRequest request){

        Map<String, String> map = new HashMap<>();
        //处理自己业务逻辑
        String token = request.getHeader("token");
        DecodedJWT claims = JWTUtil.getTokenInfo(token);
        log.info("用户id: [{}]",claims.getClaim("id").asString());
        log.info("用户name: [{}]",claims.getClaim("username").asString());
        log.info("用户role: [{}]",claims.getClaim("role").asString());
        map.put("code","200");
        map.put("msg","请求成功!");
        return map;
    }

}
