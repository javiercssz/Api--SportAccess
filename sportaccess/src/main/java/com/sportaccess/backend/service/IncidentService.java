package com.sportaccess.backend.service;

import com.sportaccess.backend.model.Incident;
import com.sportaccess.backend.model.Incident.IncidentStatus;
import com.sportaccess.backend.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public Incident reportIncident(Incident incident) {
        incident.setReportedAt(LocalDateTime.now());
        incident.setStatus(IncidentStatus.OPEN);
        return incidentRepository.save(incident);
    }

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public Incident updateIncidentStatus(Long id, IncidentStatus status) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Incident not found"));
        incident.setStatus(status);
        return incidentRepository.save(incident);
    }
}
