package com.example.common.aspect;

import com.example.common.annotation.RequiresRoles;
import com.example.common.exception.BusinessException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class RoleAspect {

    @Resource
    private HttpServletRequest request;

    @Before("@annotation(requiresRoles)")
    public void checkRole(RequiresRoles requiresRoles) {
        String userRole = request.getHeader("X-User-Role");
        List<String> allowedRoles = Arrays.asList(requiresRoles.value());
        
        // 角色层级逻辑
        if ("PRODUCT_ADMIN".equals(userRole)) {
            return; // 管理员拥有所有权限
        }
        
        if ("EDITOR".equals(userRole)) {
            if (allowedRoles.contains("EDITOR") || allowedRoles.contains("USER")) {
                return;
            }
        }
        
        if ("USER".equals(userRole)) {
            if (allowedRoles.contains("USER")) {
                return;
            }
        }

        throw new BusinessException("权限不足");
    }
}
