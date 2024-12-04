package com.example.fs19_azure.service;

import com.example.fs19_azure.entity.Users;
import com.example.fs19_azure.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    public Users createUser(Users user) {
        return usersRepository.save(user);
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }
}
