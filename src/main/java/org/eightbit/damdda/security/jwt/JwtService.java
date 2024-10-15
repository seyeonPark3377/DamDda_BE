package org.eightbit.damdda.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

@Component
public class JwtService {

    static final long EXPIRATION_TIME = 60 * 60 * 24 * 1 * 1000;
    static final String PREFIX = "Bearer ";
    static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String getToken(Long memberId, String loginId){
        String token = Jwts.builder()
                .setSubject(loginId)  // loginId를 subject로 설정
                .claim("memberId", memberId)  // memberId를 클레임에 추가
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // 토큰 만료 시간 설정
                .signWith(key)  // 서명 키 설정
                .compact();

        return token;
    }

    public String getAuthUser(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token != null && token.startsWith(PREFIX)) {
            System.out.println(token);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.replace(PREFIX, ""))
                    .getBody();
            String user = claims.getSubject();
            return user;
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            // 토큰에서 모든 클레임을 추출하면서 서명 검증을 진행
            Claims claims = extractAllClaims(token);

            // 만료 시간을 확인
            return !isTokenExpired(claims);
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    // JWT에서 만료 시간을 확인
    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.before(new Date()); // 현재 시간보다 만료 시간이 이전인지 확인
    }

    // JWT에서 모든 클레임을 추출하는 메소드
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key) // 서명할 때 사용한 키를 설정
                .parseClaimsJws(token)    // 토큰에서 클레임을 추출
                .getBody();
    }

}
