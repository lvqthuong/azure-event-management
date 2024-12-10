package com.example.fs19_azure.service;

import com.example.fs19_azure.dto.UsersCreate;
import com.example.fs19_azure.entity.Users;
import com.example.fs19_azure.exceptions.ErrorMessage;
import com.example.fs19_azure.exceptions.GlobalException;
import com.example.fs19_azure.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    public Users createUser(UsersCreate user) {
        Optional<Users> existingUser = Optional.ofNullable(usersRepository.findByEmail(user.email()));
        if (!existingUser.isEmpty()) {
            throw new GlobalException(HttpStatus.BAD_REQUEST, ErrorMessage.EMAIL_ALREADY_USED);
        }

        return usersRepository.save(
            Users.builder()
            .email(user.email())
            .password(user.password())
            .name(user.name())
            .build()
        );
    }

    public List<Users> getAllActiveUsers() {
        return usersRepository.findByDeletedFalse();
    }

    public Users getUser(UUID id) {
        return usersRepository.findById(id).orElse(null);
    }
}
