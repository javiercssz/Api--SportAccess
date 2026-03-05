package com.sportaccess.backend.controller;

import com.sportaccess.backend.model.SportsCenter;
import com.sportaccess.backend.repository.SportsCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/centers")
@RequiredArgsConstructor
public class SportsCenterController {

    private final SportsCenterRepository sportsCenterRepository;

    @GetMapping
    public ResponseEntity<List<SportsCenter>> getAllCenters() {
        return ResponseEntity.ok(sportsCenterRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<SportsCenter> createCenter(@RequestBody SportsCenter center) {
        return ResponseEntity.ok(sportsCenterRepository.save(center));
    }
}
