package com.sportaccess.backend.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO para actualizar el perfil del usuario autenticado (nombre, teléfono).
 */
@Data
public class UserUpdateRequest {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @Pattern(regexp = "^[+]?[0-9]{7,15}$", message = "Formato de teléfono no válido")
    private String telefono;
}
