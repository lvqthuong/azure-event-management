package com.example.fs19_azure.controller;

import com.example.fs19_azure.entity.Users;
import com.example.fs19_azure.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService userService;

    @PostMapping
    public Users createUser(@RequestBody Users user) {
        return userService.createUser(user);
    }

    @GetMapping
    public List<Users> getUsers() {
        return userService.getAllUsers();
    }
}
