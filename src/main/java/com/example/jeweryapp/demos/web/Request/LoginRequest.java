package com.example.jeweryapp.demos.web.Request;



public class LoginRequest {

    private String username;
    private String password;

    // 必须有默认构造函数
    public LoginRequest() {}

    // Getter 和 Setternpm i
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
