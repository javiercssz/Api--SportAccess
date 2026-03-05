package com.sportaccess.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "courts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Court {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private CourtType type;

    private Double pricePerHour;

    @ManyToOne
    @JoinColumn(name = "center_id", nullable = false)
    @JsonIgnoreProperties("courts")
    private SportsCenter sportsCenter;

    public enum CourtType {
        PADEL, TENNIS, FOOTBALL, BASKETBALL
    }
}
