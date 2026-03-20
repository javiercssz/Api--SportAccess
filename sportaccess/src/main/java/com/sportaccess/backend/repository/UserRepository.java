package com.sportaccess.backend.repository;

import com.sportaccess.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFirebaseUid(String firebaseUid);
    Optional<User> findByEmail(String email);
    List<User> findByRole(User.Role role);
    List<User> findByActivo(Boolean activo);
}

