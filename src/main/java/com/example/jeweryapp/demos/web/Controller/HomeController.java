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
}