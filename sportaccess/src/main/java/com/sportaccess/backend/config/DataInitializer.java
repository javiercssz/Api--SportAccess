package com.sportaccess.backend.config;

import com.sportaccess.backend.model.Court;
import com.sportaccess.backend.model.SportsCenter;
import com.sportaccess.backend.model.User;
import com.sportaccess.backend.repository.CourtRepository;
import com.sportaccess.backend.repository.SportsCenterRepository;
import com.sportaccess.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final SportsCenterRepository centerRepository;
    private final CourtRepository courtRepository;
    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Create a test user if none exists
            if (userRepository.count() == 0) {
                User testUser = User.builder()
                        .name("Usuario de Prueba")
                        .email("test@example.com")
                        .firebaseUid("test-uid-123")
                        .role(User.Role.CLIENT)
                        .build();
                userRepository.save(testUser);
            }

            if (centerRepository.count() == 0) {
                SportsCenter center = SportsCenter.builder()
                        .name("Polideportivo Municipal")
                        .address("Calle Principal 123")
                        .latitude(40.4167)
                        .longitude(-3.7033)
                        .contactPhone("912345678")
                        .build();
                
                centerRepository.save(center);

                Court court1 = Court.builder()
                        .name("Pista de Pádel 1")
                        .type(Court.CourtType.PADEL)
                        .pricePerHour(20.0)
                        .sportsCenter(center)
                        .build();

                Court court2 = Court.builder()
                        .name("Pista de Tenis 1")
                        .type(Court.CourtType.TENNIS)
                        .pricePerHour(15.0)
                        .sportsCenter(center)
                        .build();

                courtRepository.saveAll(Arrays.asList(court1, court2));
            }
        };
    }
}
