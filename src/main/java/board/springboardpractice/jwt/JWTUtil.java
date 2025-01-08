package board.springboardpractice.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
/*
  인가
 */
@Component
public class JWTUtil {

  private final SecretKey secretKey;

  public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
    this.secretKey = new SecretKeySpec(
            secret.getBytes(StandardCharsets.UTF_8),
            "HmacSHA256"  // 직접 알고리즘 지정
    );
  }

  public String getLoginId(String token){
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("loginId",String.class);
  }

  public String getUserRole(String token){
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userRole", String.class);
  }

  public Boolean isExpired(String token) {
    if (token == null || token.isEmpty()) {
      return true; // 토큰이 없거나 비어 있으면 만료된 것으로 간주
    }

    try {
      return Jwts.parser()
              .verifyWith(secretKey)
              .build()
              .parseSignedClaims(token)
              .getPayload()
              .getExpiration()
              .before(new Date());
    } catch (Exception e) {
      // 파싱 중 문제가 발생하면 만료된 것으로 간주
      return true;
    }
  }

  public String createJwt(String username, String role,Long expiredMs) {

    return Jwts.builder()
            .claim("loginId", username)
            .claim("userRole", role)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact();
  }
}