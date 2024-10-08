package org.eightbit.damdda.member.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

@Component
public class JwtService {

    static final long EXPIRATION_TIME = 60 * 60 * 24 * 1 * 1000;
    static final String PREFIX = "Bearer ";
    static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String getToken(String loginId){
        String token = Jwts.builder()
                .setSubject(loginId)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();

        return token;
    }

    public String getAuthUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println(token);

        if (token != null && token.startsWith(PREFIX)) {
            System.out.println(2);
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token.replace(PREFIX, ""))
                        .getBody();
                String user = claims.getSubject();
                System.out.println(user + " ddddd " + token);
                return user;
        }
        return null;
    }

}
