package unoeste.fipp.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JWTTokenProvider {

    private static final SecretKey CHAVE = Keys.hmacShaKeyFor(
            "MINHACHAVESECRETA_MINHACHAVESECRETA_SEGURANCA_TOTAL".getBytes(StandardCharsets.UTF_8));

    static public String createToken(String usuario, String nivel) {
        return Jwts.builder()
                .setSubject(usuario)
                .setIssuer("localhost:8080")
                .claim("nivel", nivel)
                .setIssuedAt(new Date())

                .setExpiration(Date.from(LocalDateTime.now().plusHours(1L)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(CHAVE)
                .compact();
    }

    static public boolean verifyToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(CHAVE)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {

            System.out.println("Token inválido ou expirado: " + e.getMessage());
            return false;
        }
    }

    static public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(CHAVE)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Erro ao recuperar as informações (claims): " + e.getMessage());
            return null;
        }
    }

    static public String getClaimFromToken(String token, String chave) {
        Claims claims = getAllClaimsFromToken(token);

        if (claims != null && claims.containsKey(chave)) {
            return claims.get(chave).toString();
        }
        return null;
    }
}