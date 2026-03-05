package com.sportaccess.backend.dto;

import lombok.Data;

@Data
public class IncidentRequest {
    private Long userId;
    private Long courtId;
    private String description;
    private String imageUrl;
}
