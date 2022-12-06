package com.project.webapp.estimatemanager.repository;

import com.project.webapp.estimatemanager.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findUserEntityById(Long id);
    Optional<UserEntity> findUserEntityByEmail(String email);
    void deleteUserEntityById(Long id);
}
