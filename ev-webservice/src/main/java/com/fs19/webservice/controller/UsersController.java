package com.fs19.webservice.controller;

import com.fs19.webservice.controller.response.GlobalResponse;
import com.fs19.webservice.dto.UsersCreate;
import com.fs19.webservice.entity.Users;
import com.fs19.webservice.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService userService;

    @PostMapping
    public ResponseEntity<GlobalResponse<Users>> createUser(@RequestBody UsersCreate dto) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.CREATED.value(),
                userService.createUser(dto)
            )
            , HttpStatus.CREATED
        );
    }

    //get all users
    @GetMapping
    public ResponseEntity<GlobalResponse<List<Users>>> getActiveUsers() {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.OK.value(),
                userService.getAllActiveUsers()
            )
            , HttpStatus.OK
        );
    }

    //get uer profile
    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<Users>> getUser(@PathVariable UUID id) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.OK.value(),
                userService.getUser(id)
            )
            , HttpStatus.OK
        );
    }
}
