package com.web.bakery.service;

import java.security.Key;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.UUID;

public class JwtService {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String SECRET_KEY = "kRR9Y9Vikv0+yydtjfb/587ER5c34qUA5sAim5c0KQ4=";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 дней в мс

    public static String generateToken(UUID userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setId(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static UUID getUserIdFromToken(String token) {
        return UUID.fromString(Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody().getId());
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
