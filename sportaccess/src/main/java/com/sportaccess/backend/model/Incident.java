package com.sportaccess.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "court_id")
    private Court court;

    @Column(nullable = false)
    private String description;

    private String imageUrl; // Firebase Storage URL

    private LocalDateTime reportedAt;

    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    public enum IncidentStatus {
        OPEN, IN_PROGRESS, RESOLVED, CLOSED
    }
}
