package com.project.webapp.estimatemanager.repository;

import com.project.webapp.estimatemanager.models.Opt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptionRepo extends JpaRepository<Opt, Long> {
    Optional<Opt> findOptById(Long id);
    Optional<Opt> findOptByName(String name);
    void deleteOptById(Long id);
}
