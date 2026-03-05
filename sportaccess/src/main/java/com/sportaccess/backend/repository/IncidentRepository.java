package com.sportaccess.backend.repository;

import com.sportaccess.backend.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByUserId(Long userId);
    List<Incident> findByStatus(Incident.IncidentStatus status);
}
