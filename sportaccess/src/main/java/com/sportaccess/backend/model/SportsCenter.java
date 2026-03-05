package com.sportaccess.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "sports_centers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SportsCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;

    private Double latitude;
    private Double longitude;

    private String contactPhone;

    @OneToMany(mappedBy = "sportsCenter", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("sportsCenter")
    private List<Court> courts;
}
