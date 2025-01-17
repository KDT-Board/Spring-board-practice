package board.springboardpractice.jwt.util;

import board.springboardpractice.domain.user.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtGenerator {
  public String generateAccessToken(final Key ACCESS_SECRET, final long ACCESS_EXPIRATION, User user) {
    Long now = System.currentTimeMillis();

    return Jwts.builder()
            .setHeader(createHeader())
            .setClaims(createClaims(user))
            .setSubject(String.valueOf(user.getId()))
            .setExpiration(new Date(now + ACCESS_EXPIRATION))
            .signWith(ACCESS_SECRET, SignatureAlgorithm.HS256)
            .compact();
  }

  public String generateRefreshToken(final Key REFRESH_SECRET, final long REFRESH_EXPIRATION, User user) {
    Long now = System.currentTimeMillis();

    return Jwts.builder()
            .setHeader(createHeader())
            .setSubject(user.getLoginId())
            .setExpiration(new Date(now + REFRESH_EXPIRATION))
            .signWith(REFRESH_SECRET, SignatureAlgorithm.HS256)
            .compact();
  }

  private Map<String, Object> createHeader() {
    Map<String, Object> header = new HashMap<>();
    header.put("typ", "JWT");
    header.put("alg", "HS256");
    return header;
  }

  private Map<String, Object> createClaims(User user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("Identifier", user.getLoginId());
    claims.put("UserRole", user.getUserRole()); //보드 접근 권한
    claims.put("Role", user.getRole()); //관리자 접근 권한
    return claims;
  }

}