package com.example.demoJwt.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

//    private static final String SECRET_KEY="56YR4A6E5RYH3AE2R1YA68WER4YA6E5R1Y";
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }
    private String getToken(Map<String, Object> extraClaims, UserDetails user){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(SECRET_KEY)
                .compact();
    }


    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
                username.equals(userDetails.getUsername())
                        && !isTokenExpired(token));
    }

    private Claims getAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token) // LEER info del metodo
                .getBody();
        // INFO DEL METODO:
        //parseClaimsJwt() se utiliza para tokens JWT que no están firmados.
        //parseClaimsJws() se utiliza para tokens JWT que están firmados.
    }
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }
    //La clave utilizada para firmar JWT debe tener al menos 256 bits (32 bytes) de longitud para cumplir con los estándares de seguridad. Por eso generamos una clave con las clase Keys de io.jsonwebtoken.security

//    private Key getKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
}
