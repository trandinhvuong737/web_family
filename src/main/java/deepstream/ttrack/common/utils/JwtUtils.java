package deepstream.ttrack.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${t-track.app.jwtSecret}")
    private String jwtSecret;

    @Value("${t-track.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    private Collection<GrantedAuthority> authorities;


    public String generateJwtToken(Authentication authentication) {
        User userPrinciple = (User) authentication.getPrincipal();
        this.authorities = userPrinciple.getAuthorities();
        List<String> privileges = this.authorities.stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        Map<String, Object> claims = new HashMap<>();
        claims.put("PRIVILEGES", privileges);
        SecretKey secret = getSecretKey();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject((userPrinciple.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(secret)
                .compact();
    }

    public Claims parseClaimsJws(String token) {
        SecretKey secret = getSecretKey();
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build().parseClaimsJws(token)
                .getBody();
    }

    public String getUserNameFromJwtToken(String token) {
        Claims claims = parseClaimsJws(token);
        return claims.getSubject();
    }

    public long getTime(String token) {
        Claims claims = parseClaimsJws(token);
        return claims.getExpiration().getTime();
    }

    public long getExpiration(String token) {
        long time = getTime(token);
        long expiredTime = new Date(time - (new Date()).getTime()).getTime();
        if (expiredTime < 0) {
            expiredTime = 0;
        }
        return expiredTime;
    }

    public boolean validateJwtToken(String authToken) {
        SecretKey secret = getSecretKey();
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    private SecretKey getSecretKey() {
        byte[] encode = Base64.getEncoder().encode(this.jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Keys.hmacShaKeyFor(encode);
    }

}
