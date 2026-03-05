package com.sportaccess.backend.controller;

import com.sportaccess.backend.model.Booking;
import com.sportaccess.backend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestParam Long userId,
            @RequestParam Long courtId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        
        Booking booking = bookingService.createBooking(
                userId, courtId, 
                LocalDateTime.parse(startTime), 
                LocalDateTime.parse(endTime));
        
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }
}
