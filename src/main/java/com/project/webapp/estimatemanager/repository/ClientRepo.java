package com.project.webapp.estimatemanager.repository;

import com.project.webapp.estimatemanager.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
    Optional<Client> findClientByEmail(String email);
    Optional<Client> findClientById(Long id);
    void deleteClientById(Long id);
}
