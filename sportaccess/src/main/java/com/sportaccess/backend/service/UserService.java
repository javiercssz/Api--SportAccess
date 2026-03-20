package com.sportaccess.backend.service;

import com.google.firebase.auth.FirebaseToken;
import com.sportaccess.backend.dto.UserResponse;
import com.sportaccess.backend.dto.UserUpdateRequest;
import com.sportaccess.backend.exception.UserNotFoundException;
import com.sportaccess.backend.model.User;
import com.sportaccess.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // ── Sincronización con Firebase (usado por el filtro JWT) ──────────────────

    @Transactional
    public User syncUser(FirebaseToken token) {
        String uid = token.getUid();
        String email = token.getEmail();
        String name = (String) token.getClaims().get("name");
        String finalName = (name != null && !name.isBlank()) ? name : email.split("@")[0];

        return userRepository.findByFirebaseUid(uid)
                .map(existingUser -> {
                    existingUser.setEmail(email);
                    existingUser.setName(finalName);
                    return userRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .firebaseUid(uid)
                            .email(email)
                            .name(finalName)
                            .role(User.Role.CLIENT)
                            .build();
                    return userRepository.save(newUser);
                });
    }

    // ── CRUD ──────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public User getEntityByFirebaseUid(String uid) {
        return userRepository.findByFirebaseUid(uid)
                .orElseThrow(() -> new UserNotFoundException("firebaseUid", uid));
    }

    @Transactional
    public UserResponse updateProfile(Long id, UserUpdateRequest dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }
        if (dto.getTelefono() != null) {
            user.setTelefono(dto.getTelefono());
        }
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse changeRole(Long id, User.Role newRole) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setRole(newRole);
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        // Soft-delete: marcar como inactivo en lugar de borrar físicamente
        user.setActivo(false);
        userRepository.save(user);
    }
}

