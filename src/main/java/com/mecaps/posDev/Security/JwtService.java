package com.mecaps.posDev.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.antlr.v4.runtime.Token;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET_KEY = "23847560128376455892947630i8458845u84984357" ;

    public SecretKey getSecretkey(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));

    }

    public String generateAccessToken(String email , String role){
       return Jwts.builder()
               .subject(email)
               .claim("Role" , role)
               .issuedAt(new Date(System.currentTimeMillis()))
               .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
               .signWith(getSecretkey())
               .compact() ;

    }

    // Extract email
    public  String extractEmail(String token){
     return extractAllClaims(token).getSubject();

    }

    // Extract role
    public String extractRole(String token){
        return extractAllClaims(token).get("Role",String.class);

    }

    //Validate token
    public boolean isTokenValid(String token){
        Claims claims= extractAllClaims(token) ;
        return claims.getExpiration().after(new Date());

    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSecretkey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }
}
