package com.sportaccess.backend.service;

import com.sportaccess.backend.model.Booking;
import com.sportaccess.backend.model.Booking.BookingStatus;
import com.sportaccess.backend.model.Court;
import com.sportaccess.backend.model.User;
import com.sportaccess.backend.repository.BookingRepository;
import com.sportaccess.backend.repository.CourtRepository;
import com.sportaccess.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CourtRepository courtRepository;
    private final UserRepository userRepository;

    @Transactional
    public Booking createBooking(Long userId, Long courtId, LocalDateTime startTime, LocalDateTime endTime) {
        // 1. Basic Validations
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Cannot book a court in the past.");
        }
        if (endTime.isBefore(startTime)) {
            throw new RuntimeException("End time must be after start time.");
        }

        // 2. Overlap Check
        List<Booking> existingBookings = bookingRepository.findByCourtId(courtId);
        boolean isOverlapping = existingBookings.stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED || b.getStatus() == BookingStatus.PENDING)
                .anyMatch(b -> startTime.isBefore(b.getEndTime()) && endTime.isAfter(b.getStartTime()));

        if (isOverlapping) {
            throw new RuntimeException("Court is already booked for this time slot.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Court court = courtRepository.findById(courtId)
                .orElseThrow(() -> new RuntimeException("Court not found"));

        Booking booking = Booking.builder()
                .user(user)
                .court(court)
                .startTime(startTime)
                .endTime(endTime)
                .status(BookingStatus.PENDING)
                .qrCode(generateBookingQR())
                .build();
        
        // Note: Real price could be calculated based on duration (endTime - startTime)
        // For now, it's the base price of the court.

        return bookingRepository.save(booking);
    }

    private String generateBookingQR() {
        // Simplification for simulation: a unique UUID that would be encoded in a QR
        return UUID.randomUUID().toString();
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Transactional
    public Booking confirmBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }
}
