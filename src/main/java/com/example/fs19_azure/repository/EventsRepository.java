package com.example.fs19_azure.repository;

import com.example.fs19_azure.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventsRepository extends JpaRepository<Events, UUID> {
    
}
