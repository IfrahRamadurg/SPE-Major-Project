package org.example.jobportal.Configurations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.example.jobportal.Entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    private long jwtExpiration=86400000;
    private long refreshExpiration=604800000;
    public String extractUserName(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("loginId", user.getLoginId());
        claims.put("role",user.getRole());
        return generateToken(claims,user );

    }

    public String generateToken(Map<String,Object> extraClaims,User user){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail()) // ptUsername is the username field in PatientLogin
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Use jwtExpiration variable
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username=extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Integer extractId(HttpServletRequest req, String idAttribute){
        String token= resolveToken(req);
        if(token!=null){
            Claims claims=extractAllClaims(token);
            return (int)claims.get(idAttribute);
        }
        return -1;
    }

    private String resolveToken(HttpServletRequest req){
        String token=req.getHeader("Authorization");
        if(token!=null && token.startsWith("Bearer ")){
            return token.substring(7);
        }
        return null;
    }

    // This set stores invalidated tokens
    private Set<String> blacklist = new HashSet<>();

    // Method to add token to the blacklist
    public void addToBlacklist(HttpServletRequest request) {
        String token=resolveToken(request);
        blacklist.add(token);
    }

    // Method to check if a token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }

    public String extractRole(String token){
        Claims claims=extractAllClaims(token);
        return (String) claims.get("role");
    }
}
