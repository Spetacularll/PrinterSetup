package com.example.jeweryapp.demos.web.Filter;

import com.example.jeweryapp.demos.web.Request.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException, ServletException, IOException {

        // 获取当前请求的路径
        String requestURI = request.getRequestURI();

        // 跳过静态资源和公开路径的验证
        if (isPublicPath(requestURI)) {
            chain.doFilter(request, response); // 放行请求
            return;
        }

        // JWT 验证逻辑
        String token = jwtTokenProvider.resolveToken(request); // 从请求中解析出 JWT 令牌
        if (token != null && jwtTokenProvider.validateJwtToken(token)) {
            // 如果令牌有效，获取用户认证信息
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth); // 将认证信息存入 SecurityContext
        }

        // 继续执行过滤器链
        chain.doFilter(request, response);
    }

    /**
     * 检查请求路径是否是公开的路径（例如静态资源、index.html 等）。
     * @param requestURI 当前请求的 URI
     * @return 如果是公开路径，返回 true；否则返回 false
     */
    private boolean isPublicPath(String requestURI) {
        return requestURI.startsWith("/assets/") ||   // Vue 静态资源路径
                requestURI.startsWith("/static/") ||   // Spring Boot 默认静态资源路径
                requestURI.equals("/") ||              // 根路径
                requestURI.equals("/index.html") ||    // Vue 主页面
                requestURI.equals("/favicon.ico") ||   // 网站图标
                requestURI.startsWith("/manifest.json"); // PWA 配置文件
    }

}