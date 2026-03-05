package com.sportaccess.backend.service;

import com.google.firebase.auth.FirebaseToken;
import com.sportaccess.backend.model.User;
import com.sportaccess.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User syncUser(FirebaseToken token) {
        String uid = token.getUid();
        String email = token.getEmail();
        String name = (String) token.getClaims().get("name");
        String finalName = (name != null) ? name : email.split("@")[0];

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
}
