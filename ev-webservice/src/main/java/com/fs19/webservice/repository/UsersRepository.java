package com.fs19.webservice.repository;

import com.fs19.webservice.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, UUID> {
    Users findByEmail(String email);

    List<Users> findByDeletedFalse();
}

