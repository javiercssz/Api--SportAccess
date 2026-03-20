package com.sportaccess.backend.dto;

import com.sportaccess.backend.model.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para exponer datos del usuario sin revelar firebaseUid u otros internos.
 */
@Data
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String telefono;
    private User.Role role;
    private LocalDateTime fechaRegistro;
    private Boolean activo;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .telefono(user.getTelefono())
                .role(user.getRole())
                .fechaRegistro(user.getFechaRegistro())
                .activo(user.getActivo())
                .build();
    }
}
