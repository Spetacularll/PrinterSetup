package com.example.jeweryapp.demos.web.Config;

import com.example.jeweryapp.demos.web.Filter.JwtAuthenticationFilter;
import com.example.jeweryapp.demos.web.Request.JwtTokenProvider;
import com.example.jeweryapp.demos.web.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    // 定义 AuthenticationManager Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()  // 允许跨域请求
                .and()
                .csrf().disable()  // 如果使用 JWT，可以禁用 CSRF
                .formLogin().disable()  // 禁用表单登录
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 不创建会话，JWT 认证是无状态的
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/",                // 首页
                        "/index.html",      // Vue 项目打包后的主页面
                        "/assets/**",       // Vue 打包后静态资源路径
                        "/static/**",       // 其他静态资源路径
                        "/favicon.ico",     // 网站图标
                        "/manifest.json",    // PWA 或 Vue 配置的资源
                        "/login"
                ).permitAll()          // 允许匿名访问
                .antMatchers("/generate-and-print-barcode").hasRole("ADMIN")  // 只有 ADMIN 角色可以访问
                .anyRequest().permitAll()  // 其他所有请求都需要认证
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtTokenProvider));  // 添加 JWT 过滤器

        return http.build();
    }

    // CORS 配置
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173");  // 前端域名
        configuration.addAllowedOrigin("http://8.138.89.11:8080"); // 允许的来源
        configuration.addAllowedMethod("*");  // 允许所有方法
        configuration.addAllowedHeader("*");  // 允许所有请求头
        configuration.setAllowCredentials(true);  // 允许发送 cookie
        configuration.addExposedHeader("Authorization");  // 暴露 Authorization 头
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
