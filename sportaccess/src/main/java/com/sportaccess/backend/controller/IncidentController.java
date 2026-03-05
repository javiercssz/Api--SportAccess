package com.sportaccess.backend.controller;

import com.sportaccess.backend.dto.IncidentRequest;
import com.sportaccess.backend.model.Incident;
import com.sportaccess.backend.model.User;
import com.sportaccess.backend.model.Court;
import com.sportaccess.backend.service.IncidentService;
import com.sportaccess.backend.repository.UserRepository;
import com.sportaccess.backend.repository.CourtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;
    private final UserRepository userRepository;
    private final CourtRepository courtRepository;

    @PostMapping
    public ResponseEntity<Incident> reportIncident(@RequestBody IncidentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Court court = null;
        if (request.getCourtId() != null) {
            court = courtRepository.findById(request.getCourtId())
                    .orElseThrow(() -> new RuntimeException("Court not found"));
        }

        Incident incident = Incident.builder()
                .user(user)
                .court(court)
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

        return ResponseEntity.ok(incidentService.reportIncident(incident));
    }

    @GetMapping
    public ResponseEntity<List<Incident>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Incident> updateStatus(@PathVariable Long id, @RequestParam Incident.IncidentStatus status) {
        return ResponseEntity.ok(incidentService.updateIncidentStatus(id, status));
    }
}
