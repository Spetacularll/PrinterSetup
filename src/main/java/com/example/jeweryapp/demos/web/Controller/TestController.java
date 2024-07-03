package com.example.jeweryapp.demos.web.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

@RestController
public class TestController {

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/test")
    public String test() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        return objectMapper.writeValueAsString(now);
    }
}
