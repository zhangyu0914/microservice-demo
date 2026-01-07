package com.example.gateway.filter;

import com.example.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        
        // 放行登录接口
        if (path.contains("/auth/login") || path.contains("/auth/ldap-login") || path.contains("/auth/github") || path.contains("/login.html") || path.contains("/index.html")|| path.contains("/auth/github/callback") || path.contains("/v3/api-docs") || path.contains("/doc.html")) {
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                Claims claims = JwtUtils.parseToken(token);
                String role = claims.get("role", String.class);
                
                // 将角色信息传递给下游服务
                ServerHttpRequest request = exchange.getRequest().mutate()
                        .header("X-User-Role", role)
                        .build();
                return chain.filter(exchange.mutate().request(request).build());
            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
