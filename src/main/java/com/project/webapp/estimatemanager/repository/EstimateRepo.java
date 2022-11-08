package com.project.webapp.estimatemanager.repository;

import com.project.webapp.estimatemanager.models.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstimateRepo extends JpaRepository<Estimate, Long> {
    Optional<Estimate> findEstimateById(Long id);
    void deleteEstimateById(Long id);
    //Optional<Estimate> findEstimateByClient(Long client_id);
    //Optional<Estimate> findEstimateByEmployee(Long employee_id);
}
