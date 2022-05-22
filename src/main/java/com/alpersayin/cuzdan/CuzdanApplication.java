package com.alpersayin.cuzdan;

import com.alpersayin.cuzdan.entity.ResourceData;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CuzdanApplication {

    public static void main(String[] args) { SpringApplication.run(CuzdanApplication.class, args); }

}
