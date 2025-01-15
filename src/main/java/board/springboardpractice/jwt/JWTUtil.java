package board.springboardpractice.jwt;

import board.springboardpractice.global.exception.BusinessException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;

import static board.springboardpractice.global.exception.BusinessExceptionStatus.INVALID_EXPIRED_JWT;
import static board.springboardpractice.global.exception.BusinessExceptionStatus.INVALID_JWT;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JWTUtil {

  public TokenStatus getTokenStatus(String token, Key secretKey) throws BusinessException {
    try {
      Jwts.parserBuilder()
              .setSigningKey(secretKey)
              .build()
              .parseClaimsJws(token);
      return TokenStatus.AUTHENTICATED;
    } catch (ExpiredJwtException | IllegalArgumentException e) {
      log.error(INVALID_EXPIRED_JWT.getMessage());
      return TokenStatus.EXPIRED;
    } catch (JwtException e) {
      throw new BusinessException(INVALID_JWT);
    }
  }

  public String resolveTokenFromCookie(Cookie[] cookies, JwtRule tokenPrefix) {
    return Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals(tokenPrefix.getValue()))
            .findFirst()
            .map(Cookie::getValue)
            .orElse("");
  }

  public Key getSigningKey(String secretKey) {//서명키를 생성하는 역할
    String encodedKey = encodeToBase64(secretKey);
    return Keys.hmacShaKeyFor(encodedKey.getBytes(StandardCharsets.UTF_8));
  }

  private String encodeToBase64(String secretKey) {
    return Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public Cookie resetToken(JwtRule tokenPrefix) {
    Cookie cookie = new Cookie(tokenPrefix.getValue(), null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    return cookie;
  }
//
//  private final SecretKey secretKey;
//
//  public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
//    this.secretKey = new SecretKeySpec(
//            secret.getBytes(StandardCharsets.UTF_8),
//            "HmacSHA256"  // 직접 알고리즘 지정
//    );
//  }
//
//  public String getLoginId(String token){
//    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("loginId",String.class);
//  }
//
//  public String getUserRole(String token){
//    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userRole", String.class);
//  }
//
//  public Boolean isExpired(String token) {
//    if (token == null || token.isEmpty()) {
//      return true; // 토큰이 없거나 비어 있으면 만료된 것으로 간주
//    }
//
//    try {
//      return Jwts.parser()
//              .verifyWith(secretKey)
//              .build()
//              .parseSignedClaims(token)
//              .getPayload()
//              .getExpiration()
//              .before(new Date());
//    } catch (Exception e) {
//      // 파싱 중 문제가 발생하면 만료된 것으로 간주
//      return true;
//    }
//  }
//
//  public String createJwt(String username, String role,Long expiredMs) {
//
//    return Jwts.builder()
//            .claim("loginId", username)
//            .claim("userRole", role)
//            .issuedAt(new Date(System.currentTimeMillis()))
//            .expiration(new Date(System.currentTimeMillis() + expiredMs))
//            .signWith(secretKey)
//            .compact();
//  }
}