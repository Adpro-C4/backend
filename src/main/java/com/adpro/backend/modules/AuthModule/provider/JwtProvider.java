package com.adpro.backend.modules.authmodule.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class JwtProvider {

    private final Map<String, String> tokens;
    private static JwtProvider instance;
    private final Key secretKey;
    private final Set<String> revokedTokens;

    private JwtProvider() {
        secretKey  = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        revokedTokens = ConcurrentHashMap.newKeySet();
        tokens = new ConcurrentHashMap<>();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(12);
        scheduler.scheduleAtFixedRate(this::cleanUpExpiredTokens, 0, 1, TimeUnit.HOURS);
    }

    public static synchronized JwtProvider getInstance() {
        if (instance == null) {
            instance = new JwtProvider();
        }
        return instance;
    }

    public void cleanUpExpiredTokens() {
        Date now = new Date();
        revokedTokens.removeIf(token -> {
            try {
                Claims claims = parseJwtToken(token);
                Date expirationDate = claims.getExpiration();
                return expirationDate != null && expirationDate.before(now);
            } catch (Exception e) {
                return true;
            }
        });
        tokens.entrySet().removeIf(entry -> {
            try {
                Claims claims = parseJwtToken(entry.getValue());
                Date expirationDate = claims.getExpiration();
                return expirationDate != null && expirationDate.before(now);
            } catch (Exception e) {
                return true;
            }
        });

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
