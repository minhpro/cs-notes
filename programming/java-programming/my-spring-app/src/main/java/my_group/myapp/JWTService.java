package my_group.myapp;

import io.jsonwebtoken.Jwts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.KeyPair;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public class JWTService {
    private static Logger log = LogManager.getLogger(JWTService.class);
    private KeyPair keyPair;
    int expiresIn = 600; // seconds

    public JWTService(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public Optional<String> parseTokenSubject(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .verifyWith(keyPair.getPublic())
                    .build().parseSignedClaims(token).getPayload().getSubject());
        } catch (Exception e) {
            log.debug("JWT token error {}", e.getMessage());
            return Optional.empty();
        }
    }

    public TokenData generateToken(String subject) {
        String token = Jwts.builder()
                .subject(subject)
                .expiration(Date.from(Instant.ofEpochSecond(Instant.now().getEpochSecond() + expiresIn)))
                .signWith(keyPair.getPrivate(), Jwts.SIG.EdDSA)
                .compact();
        return new TokenData(token, expiresIn);
    }

}
