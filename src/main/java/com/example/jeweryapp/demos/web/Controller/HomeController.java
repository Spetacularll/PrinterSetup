package com.example.jeweryapp.demos.web.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home"; // home.html in src/main/resources/templates
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // login.html in src/main/resources/templates
    }
    @GetMapping("/audit-logs")
    public String viewAuditLogsPage() {
        return "audit-logs"; // 返回模板文件名，不需要文件扩展名
    }

}