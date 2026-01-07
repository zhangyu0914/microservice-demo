package com.example.user.controller;

import com.example.common.model.Result;
import com.example.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/auth")
@RefreshScope
public class AuthController {

    @Value("${github.client-id}")
    private String clientId;

    @Value("${gateway.url}")
    private String gatewayUrl ;

    @Resource
    private UserService userService;

    @Operation(summary = "数据库登录获取Token")
    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestParam String username, @RequestParam String password) {
        return Result.success(userService.login(username,password));
    }

    @Operation(summary = "LDAP登录获取Token")
    @PostMapping("/ldap-login")
    public Result<Map<String, String>> ldapLogin(@RequestParam String username, @RequestParam String password) {
        return Result.success(userService.ldapLogin(username, password));
    }



    @Operation(summary = "GitHub登录重定向")
    @GetMapping("/github")
    public void githubRedirect(HttpServletResponse response) throws IOException {
        String url = "https://github.com/login/oauth/authorize?client_id=" + clientId;
        response.sendRedirect(url);
    }

    @Operation(summary = "GitHub登录回调")
    @GetMapping("/github/callback")
    public void githubCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        Map<String, String> result = userService.githubLogin(code);
        String token = result.get("token");
        // 重定向回登录页面，并带上 token
        response.sendRedirect(gatewayUrl +"/user/index.html?token=" + token);
    }
}
