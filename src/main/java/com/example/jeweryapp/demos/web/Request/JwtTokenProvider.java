package com.example.jeweryapp.demos.web.Request;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // 通过 @Value 注解读取 application.properties 中的 jwt.secret 值
    @Value("${jwt.secret}")
    private String jwtSecret;
    @Autowired
    private UserDetailsService userDetailsService;

    // 设置 token 的有效期（例如：一天）
    private long jwtExpirationMs = 86400000;

    // 生成 JWT Token
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // 设置用户名为 subject
                .setIssuedAt(new Date())               // 设置 token 生成时间
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs)) // 设置 token 过期时间
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // 使用 HS512 算法签名，并使用 jwtSecret 密钥
                .compact();
    }
    // 从请求头解析 JWT 令牌
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 验证 JWT 令牌
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 从 JWT 令牌中获取用户认证信息
    public Authentication getAuthentication(String token) {
        String username = getUsernameFromJwtToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    // 从 JWT 令牌中获取用户名
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
}