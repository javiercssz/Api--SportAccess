package com.sportaccess.backend.repository;

import com.sportaccess.backend.model.SportsCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportsCenterRepository extends JpaRepository<SportsCenter, Long> {
}
