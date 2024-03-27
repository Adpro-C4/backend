package com.adpro.backend.modules.authmodule.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class JwtProvider {

    private  Map<String, String> tokens;
    private static JwtProvider instance;
    private final Key secretKey;
    private final Set<String> revokedTokens;

    private JwtProvider() {
        secretKey  = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        revokedTokens = ConcurrentHashMap.newKeySet();
        tokens = new ConcurrentHashMap<>();
    }

    public static synchronized JwtProvider getInstance() {
        if (instance == null) {
            instance = new JwtProvider();
        }
        return instance;
    }

    public String createJwtToken(String subject) {
        Date expirationDate = new Date(System.currentTimeMillis() + 6 * 60 * 60 * 1000);

        String token = Jwts.builder()
                .setSubject(subject)
                .setExpiration(expirationDate)
                .signWith(secretKey)
                .compact();
        tokens.put(subject, token);
        return  token;
    }

    public Claims parseJwtToken(String token) {
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
        return jws.getBody();
    }

    public boolean isJwtTokenValid(String token){
        if(revokedTokens.contains(token)){
            return  false;
        }
        try{
            parseJwtToken(token);
            return  true;
        }catch (Exception err){
            return  false;
        }
    }

    public String getDataFromJwt(String jwt) {
        if(revokedTokens.contains(jwt)){
            return  null;
        }
        return parseJwtToken(jwt).getSubject();
    }

    public  String getToken(String key){
        return  tokens.get(key);
    }

    public void revokeJwtToken(String token) {
        revokedTokens.add(token);
    }

}
