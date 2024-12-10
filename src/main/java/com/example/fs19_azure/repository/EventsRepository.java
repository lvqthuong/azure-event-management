package com.example.fs19_azure.repository;

import com.example.fs19_azure.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EventsRepository extends JpaRepository<Events, UUID> {
    @Modifying
    @Query(
        value = "UPDATE events SET deleted = true WHERE id = :id"
        , nativeQuery = true
    )
    int deleteByIdSoft(@Param("id") UUID id);

    List<Events> findByDeletedFalse();
}
