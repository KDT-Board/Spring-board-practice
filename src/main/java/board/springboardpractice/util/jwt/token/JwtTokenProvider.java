package board.springboardpractice.util.jwt.token;

import board.springboardpractice.api.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
  private final SecretKey key;
  private final String accessTokenExpiresIn;
  private final String refreshTokenExpiresIn;

  public JwtTokenProvider(
          @Value("${jwt.secret}") String secretKey,
          @Value("${jwt.access-expiration}") String accessTokenExpiresIn,
          @Value("${jwt.refresh-expiration}") String refreshTokenExpiresIn
  ) {
    this.accessTokenExpiresIn = accessTokenExpiresIn;
    this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  //User 정보를 가지고 accesstoken, refreshtoken을 생성하는 메서드
  public JwtToken generateToken(Authentication authentication) {
    //권한 가져오기
    String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    long now = (new Date()).getTime();

    //accesstoken 생성
    Date tokenExpiresIn1 = new Date(now + Long.parseLong(accessTokenExpiresIn));
    String accessToken = Jwts.builder()
            .subject(authentication.getName())
            .claim("auth", authorities)
            .issuedAt(new Date(now))
            .expiration(tokenExpiresIn1)
            .signWith(key)
            .compact();

    //refreshtoken 생성
    Date tokenExpiresIn2 = new Date(now + Long.parseLong(refreshTokenExpiresIn));
    String refreshToken = Jwts.builder()
            .expiration(tokenExpiresIn2)
            .signWith(key)
            .compact();

    return JwtToken.builder()
            .grantType("Bearer")
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
  }

  //Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
  public Authentication getAuthentication(String accessToken){
    //Jwt 토큰 복호화
    Claims claims = parseClaims(accessToken);
    if (claims.get("auth") == null){
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    //클레임에서 권한 정보 가져오기
    Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get("auth").toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

    // UserDetails 객체를 만들어서 Authentication return
    // UserDetails: interface, User: UserDetails를 구현한 class
    UserDetails principal = new User(claims.getSubject(), "", authorities);
    return new UsernamePasswordAuthenticationToken(principal, "", authorities);
  }

  // 토큰 정보를 검증하는 메서드
  public boolean validateToken(String token) {
    try {
      Jwts.parser()
              .verifyWith(key)
              .build()
              .parseSignedClaims(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT Token", e);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
    }
    return false;
  }



  // accessToken
  private Claims parseClaims(String accessToken) {
    try {
      return Jwts.parser()
              .verifyWith(key)
              .build()
              .parseSignedClaims(accessToken)
              .getPayload();
    } catch (ExpiredJwtException e) {
      return e.getClaims();
    }
  }
}