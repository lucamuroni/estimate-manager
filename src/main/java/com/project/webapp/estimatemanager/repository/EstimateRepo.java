package com.project.webapp.estimatemanager.repository;

import com.project.webapp.estimatemanager.models.Client;
import com.project.webapp.estimatemanager.models.Employee;
import com.project.webapp.estimatemanager.models.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstimateRepo extends JpaRepository<Estimate, Long> {
    Optional<Estimate> findEstimateById(Long id);
    void deleteEstimateById(Long id);
    List<Estimate> findEstimatesByClient(Client client);
    List<Estimate> findEstimatesByEmployee(Employee employee);
}
