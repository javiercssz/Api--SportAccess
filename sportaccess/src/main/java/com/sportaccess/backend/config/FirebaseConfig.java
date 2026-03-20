package com.sportaccess.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Configura Firebase Admin SDK.
 * - Si firebase.config.path empieza por "classpath:", carga desde el jar.
 * - Si es una ruta de archivo absoluta, carga desde el sistema de archivos.
 * - Si no está configurado, Firebase no se inicializa (útil en local/tests).
 * Responsable: Miembro 1 – T-02
 */
@Configuration
public class FirebaseConfig {

    private static final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${firebase.config.path:}")
    private String configPath;

    @PostConstruct
    public void initialize() {
        if (!StringUtils.hasText(configPath)) {
            log.warn("Firebase no configurado: firebase.config.path está vacío. " +
                     "La autenticación con Firebase estará deshabilitada.");
            return;
        }

        if (FirebaseApp.getApps().isEmpty()) {
            try (InputStream credentialsStream = resolveCredentialsStream()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                        .build();
                FirebaseApp.initializeApp(options);
                log.info("Firebase Admin SDK inicializado correctamente.");
            } catch (IOException e) {
                log.error("Error al inicializar Firebase Admin SDK: {}", e.getMessage(), e);
            }
        }
    }

    private InputStream resolveCredentialsStream() throws IOException {
        if (configPath.startsWith("classpath:")) {
            String resourcePath = configPath.substring("classpath:".length());
            return new ClassPathResource(resourcePath).getInputStream();
        }
        return new FileInputStream(configPath);
    }
}
