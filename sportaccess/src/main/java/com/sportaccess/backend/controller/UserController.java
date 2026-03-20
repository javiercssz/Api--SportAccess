package com.sportaccess.backend.controller;

import com.sportaccess.backend.dto.UserResponse;
import com.sportaccess.backend.dto.UserUpdateRequest;
import com.sportaccess.backend.model.User;
import com.sportaccess.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de usuarios.
 * Responsable: Miembro 1 – Backend Lead (T-03)
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios de SportAccess")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    /**
     * GET /api/users/me
     * Devuelve el perfil del usuario autenticado.
     */
    @GetMapping("/me")
    @Operation(summary = "Obtener perfil del usuario autenticado")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(UserResponse.from(currentUser));
    }

    /**
     * PUT /api/users/me
     * Actualiza el perfil del usuario autenticado (nombre y teléfono).
     */
    @PutMapping("/me")
    @Operation(summary = "Actualizar perfil del usuario autenticado")
    public ResponseEntity<UserResponse> updateMe(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(userService.updateProfile(currentUser.getId(), request));
    }

    /**
     * GET /api/users
     * Lista todos los usuarios. Solo ADMIN.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos los usuarios (solo ADMIN)")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    /**
     * GET /api/users/{id}
     * Obtiene un usuario por ID. ADMIN o el propio usuario.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID (ADMIN o propio usuario)")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        boolean isAdmin = currentUser.getRole() == User.Role.ADMIN;
        boolean isOwnProfile = currentUser.getId().equals(id);

        if (!isAdmin && !isOwnProfile) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(userService.getById(id));
    }

    /**
     * PATCH /api/users/{id}/role
     * Cambia el rol de un usuario. Solo ADMIN.
     */
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cambiar el rol de un usuario (solo ADMIN)")
    public ResponseEntity<UserResponse> changeRole(
            @PathVariable Long id,
            @RequestParam User.Role role) {
        return ResponseEntity.ok(userService.changeRole(id, role));
    }

    /**
     * DELETE /api/users/{id}
     * Soft-delete de un usuario. Solo ADMIN.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desactivar un usuario (soft-delete, solo ADMIN)")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
