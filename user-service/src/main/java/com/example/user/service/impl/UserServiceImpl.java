package com.example.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.exception.BusinessException;
import com.example.common.utils.JwtUtils;
import com.example.user.entity.User;
import com.example.user.mapper.UserMapper;
import com.example.user.service.LdapService;
import com.example.user.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService {

    @Resource
    private LdapService ldapService;

    @Override
    public Map<String, String> login(String username, String password) {
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getPassword, password),false);
        Assert.isTrue(user != null,"用户名或密码错误！");
            String token = JwtUtils.createToken(user.getUsername(), user.getRole());
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
        return map;
    }

    @Override
    public Map<String, String> ldapLogin(String username, String password) {
        if (ldapService.authenticate(username, password)) {
            String role = ldapService.getRole(username);
            String token = JwtUtils.createToken(username, role);
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            return map;
        }
        throw new BusinessException("LDAP 认证失败");
    }

    @Value("${github.client-id}")
    private String clientId;

    @Value("${github.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, String> githubLogin(String code) {
        // 1. 换取 access_token
        String tokenUrl = "https://github.com/login/oauth/access_token";
        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        Map<String, Object> responseBody = response.getBody();
        String accessToken = (String) responseBody.get("access_token");

        if (accessToken == null) {
            throw new BusinessException("GitHub 登录失败：无法获取 access_token");
        }

        // 2. 获取用户信息
        String userUrl = "https://api.github.com/user";
        headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, entity, Map.class);
        Map<String, Object> githubUser = userResponse.getBody();
        String githubId = String.valueOf(githubUser.get("id"));
        String login = (String) githubUser.get("login");

        // 3. 查找或创建本地用户
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getGithubId, githubId));
        if (user == null) {
            user = new User();
            user.setUsername(login);
            user.setGithubId(githubId);
            user.setRole("USER");
            this.save(user);
        }

        // 4. 生成 Token
        String token = JwtUtils.createToken(user.getUsername(), user.getRole());
        Map<String, String> result = new HashMap<>();
        result.put("token", token);
        return result;
    }
}
