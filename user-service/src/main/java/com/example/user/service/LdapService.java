package com.example.user.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Slf4j
@Service
public class LdapService {

    @Resource
    private LdapTemplate ldapTemplate;

    public boolean authenticate(String username, String password) {
        log.info("Attempting LDAP authentication for user: {}", username);
        try {
            // 1. 构建查询条件
            // 注意：query().where("uid").is(username) 会自动处理转义，防止 LDAP 注入
            // 它会相对于 application.yml 中配置的 base (dc=example,dc=org) 进行搜索
            ldapTemplate.authenticate(query().where("uid").is(username), password);

            log.info("LDAP authentication successful for user: {}", username);
            return true;
        } catch (org.springframework.ldap.AuthenticationException e) {
            // 认证失败：通常是用户名或密码错误
            log.warn("LDAP authentication failed: Invalid credentials for user {}. Error: {}", username, e.getMessage());
            return false;
        } catch (org.springframework.ldap.NamingException e) {
            // 命名异常：可能是用户不存在，或者管理账号权限不足无法搜索到该用户
            log.error("LDAP naming error during authentication for user {}: {}. This might indicate user not found or insufficient bind DN permissions.", username, e.getMessage());
            return false;
        } catch (Exception e) {
            // 其他异常：如连接超时、服务器不可达等
            log.error("Unexpected error during LDAP authentication for user {}: {}. Full stack trace: ", username, e.getMessage(), e);
            return false;
        }
    }

    public String getRole(String username) {
        // LDAP 角色映射逻辑
        if (username.contains("adm")) {
            return "PRODUCT_ADMIN";
        }
        if (username.contains("editor")) {
            return "EDITOR";
        }
        if (username.contains("user")) {
            return "USER";
        }
        return "USER";
    }
}