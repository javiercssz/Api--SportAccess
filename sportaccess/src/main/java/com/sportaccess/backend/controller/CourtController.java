package com.sportaccess.backend.controller;

import com.sportaccess.backend.model.Court;
import com.sportaccess.backend.repository.CourtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courts")
@RequiredArgsConstructor
public class CourtController {

    private final CourtRepository courtRepository;

    @GetMapping
    public ResponseEntity<List<Court>> getAllCourts() {
        return ResponseEntity.ok(courtRepository.findAll());
    }

    @GetMapping("/center/{centerId}")
    public ResponseEntity<List<Court>> getByCenter(@PathVariable Long centerId) {
        return ResponseEntity.ok(courtRepository.findBySportsCenterId(centerId));
    }
}
