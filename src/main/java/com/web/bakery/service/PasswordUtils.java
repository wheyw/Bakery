package com.web.bakery.service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Метод для хэширования пароля
    public static String encodePassword(String password) {
        return encoder.encode(password);
    }

    // Метод для проверки пароля на соответствие хэшу
    public static boolean verifyPassword(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
