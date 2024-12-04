package com.example.fs19_azure.repository;

import com.example.fs19_azure.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, UUID> {
}

