package com.project.webapp.estimatemanager.repository;

import com.project.webapp.estimatemanager.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
}
