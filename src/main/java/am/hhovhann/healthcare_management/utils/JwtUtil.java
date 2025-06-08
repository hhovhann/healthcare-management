package am.hhovhann.healthcare_management.utils;

import am.hhovhann.healthcare_management.config.SecretKeyConfiguration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component // Marks this class as a Spring Component
public class JwtUtil {

    private final SecretKeyConfiguration secretKeyConfiguration;

    public JwtUtil(SecretKeyConfiguration secretKeyConfiguration) {
        this.secretKeyConfiguration = secretKeyConfiguration;
    }

    // Extracts the username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extracts expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extracts a specific claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extracts all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    // Checks if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generates a JWT token for a UserDetails object
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // You can add custom claims here, e.g., user roles
        claims.put(
                "roles",
                userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        return createToken(claims, userDetails.getUsername());
    }

    // Creates the JWT token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // The subject of the token (usually username)
                .setIssuedAt(new Date(System.currentTimeMillis())) // When the token was issued
                .setExpiration(
                        new Date(System.currentTimeMillis() + secretKeyConfiguration.getSecretKeyExpiration())) // Token expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Signing algorithm and key
                .compact(); // Builds the token
    }

    // Validates the token against UserDetails and checks for expiration
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Retrieves the signing key from the secret string
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyConfiguration.getSecretKey()));
    }
    public static void main(String[] args) {
        // For HS256, a key of 32 bytes (256 bits) is required
        Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
        String secretString = Encoders.BASE64URL.encode(key.getEncoded());
        System.out.println("New JWT Secret (for HS256): " + secretString);
    }
}
