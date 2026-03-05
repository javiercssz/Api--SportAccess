package com.sportaccess.backend.service;

import com.sportaccess.backend.model.Booking;
import com.sportaccess.backend.model.Booking.BookingStatus;
import com.sportaccess.backend.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccessControlService {

    private final BookingRepository bookingRepository;

    public boolean validateAccess(Long bookingId, String qrCode, double userLat, double userLon) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // 1. Validate QR Code
        if (!booking.getQrCode().equals(qrCode)) {
            return false;
        }

        // 2. Validate Time Slot (e.g., allow entry 10 mins before and until the end)
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(booking.getStartTime().minusMinutes(10)) || now.isAfter(booking.getEndTime())) {
            return false;
        }

        // 3. Validate Geofencing (Simulated business logic)
        // Center coordinates should be fetched from booking -> court -> sportsCenter
        double centerLat = booking.getCourt().getSportsCenter().getLatitude();
        double centerLon = booking.getCourt().getSportsCenter().getLongitude();
        
        double distance = calculateDistance(userLat, userLon, centerLat, centerLon);
        
        // Allow access if within 100 meters
        return distance <= 100.0;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula simplification or basic Euclidean for simulation
        double latDiff = lat1 - lat2;
        double lonDiff = lon1 - lon2;
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff) * 111000; // rough meters
    }
}
