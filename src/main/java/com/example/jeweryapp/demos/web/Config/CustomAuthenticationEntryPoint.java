package com.example.jeweryapp.demos.web.Config;

//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    @Override
//    public void commence(HttpServletRequest request,
//                         HttpServletResponse response,
//                         AuthenticationException authException) throws IOException {
//        // 判断请求是否为 AJAX 请求
//        String ajaxHeader = request.getHeader("X-Requested-With");
//        if ("XMLHttpRequest".equals(ajaxHeader)) {
//            // 返回 JSON 形式的 401 状态码，而不是重定向
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write("{\"error\":\"Unauthorized\"}");
//        } else {
//            // 对于非 AJAX 请求，可以继续执行默认行为（重定向到登录页面）
//            response.sendRedirect("/login");
//        }
//    }
//}
