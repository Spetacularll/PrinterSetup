package com.example.jeweryapp;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;
import java.util.Base64;

public class BCryptEncoderTest {


    static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Test
    public  void test(){
        String encodedString = bCryptPasswordEncoder.encode("7sevenG");
        System.out.println(encodedString);
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // HS512 算法
        String base64EncodedSecretKey = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println(base64EncodedSecretKey); // 打印 Base64 编码后的密钥

    }
}
