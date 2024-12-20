package com.fs19.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    private void initializeModel(Model model, OAuth2AuthenticationToken token) {
        if (token != null) {
            final OAuth2User user = token.getPrincipal();
            final OAuth2AuthorizedClient authorizedClient =
                    authorizedClientService.loadAuthorizedClient(
                            token.getAuthorizedClientRegistrationId(),
                            token.getName());
            String accessToken=authorizedClient.getAccessToken().getTokenValue();
            System.out.println(accessToken);
            model.addAttribute("accessToken", accessToken);
            model.addAttribute("grant_type", user.getAuthorities());
            model.addAllAttributes(user.getAttributes());
        }
    }

    @GetMapping(value = "/")
    public String index(Model model, OAuth2AuthenticationToken token) {
        initializeModel(model, token);

        return "home";
    }


    @GetMapping(value = "/home")
    public String home(Model model, OAuth2AuthenticationToken token) {
        initializeModel(model, token);

        return "home";
    }
}
