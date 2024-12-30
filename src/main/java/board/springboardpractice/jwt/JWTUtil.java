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

  private SecretKey secretKey;

  public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
    secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  public String getLoginId(String token){
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("loginId",String.class);
  }

  public String getUserRole(String token){
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userRole", String.class);
  }

  public Boolean isExpired(String token){
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
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