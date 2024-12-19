package com.fs19.webservice.service;

import com.fs19.webservice.dto.UsersCreate;
import com.fs19.webservice.entity.Users;
import com.fs19.webservice.exceptions.ErrorMessage;
import com.fs19.webservice.exceptions.GlobalException;
import com.fs19.webservice.repository.UsersRepository;
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
