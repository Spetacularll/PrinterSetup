package com.example.jeweryapp;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptEncoderTest {


    static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Test
    public  void test(){
        String encodedString = bCryptPasswordEncoder.encode("123");
        System.out.println(encodedString);
    }
}
