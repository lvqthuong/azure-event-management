package com.fs19.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class TestController {

    @GetMapping("/token")
    public ResponseEntity<Map<String,String>> token(OAuth2AuthenticationToken token){
        System.out.println(token.toString());
        return ResponseEntity.ok(Map.of("token", token.toString()));
    }
}
