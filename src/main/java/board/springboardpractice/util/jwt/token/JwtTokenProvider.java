package board.springboardpractice.util.jwt.token;

import board.springboardpractice.api.entity.user.domain.User;
import board.springboardpractice.api.entity.user.application.ReissueService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.rmi.AccessException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
  private final SecretKey key;  // SecretKey 객체로 수정
  private final String accessTokenExpiresIn;
  private final String refreshTokenExpiresIn;
  private final ReissueService reissueService;

  public JwtTokenProvider(
          @Value("${jwt.secret}") String secretKey,
          @Value("${jwt.access-expiration}") String accessTokenExpiresIn,
          @Value("${jwt.refresh-expiration}") String refreshTokenExpiresIn,
          ReissueService reissueService) {
    this.accessTokenExpiresIn = accessTokenExpiresIn;
    this.refreshTokenExpiresIn = refreshTokenExpiresIn;

    log.info("Secret key before decode: {}", secretKey);
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    log.info("Decoded key length: {} bytes", keyBytes.length);
    this.key = Keys.hmacShaKeyFor(keyBytes);  // SecretKey로 변환
    this.reissueService = reissueService;
  }

  // User 정보를 가지고 accesstoken, refreshtoken을 생성하는 메서드
  public JwtToken generateToken(Authentication authentication) {
    // 권한 가져오기
    String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    long now = (new Date()).getTime();

    // accesstoken 생성
    String accessToken = makeAccessToken(authentication, now, authorities);

    // refreshtoken 생성
    String refreshToken = makeRefreshToken(now);

    return JwtToken.builder()
            .grantType("Bearer")
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
  }

  private String makeAccessToken(Authentication authentication, long now, String authorities) {
    Date tokenExpiresIn1 = new Date(now + Long.parseLong(accessTokenExpiresIn));
    String accessToken = Jwts.builder()
            .setSubject(authentication.getName())
            .claim("auth", authorities)
            .setIssuedAt(new Date(now))
            .setExpiration(tokenExpiresIn1)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    return accessToken;
  }

  private String makeRefreshToken(long now) {
    Date tokenExpiresIn2 = new Date(now + Long.parseLong(refreshTokenExpiresIn));
    Claims claims = Jwts
            .claims();
    String refreshToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date(now))
            .setExpiration(tokenExpiresIn2)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    return refreshToken;
  }

  // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
  public Authentication getAuthentication(String accessToken) {
    // Jwt 토큰 복호화
    Claims claims = getUserInfoFromToken(accessToken);
    if (claims.get("auth") == null) {
      throw new RuntimeException("권한 정보가 없는 토큰입니다.");
    }

    // 클레임에서 권한 정보 가져오기
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
  public String validateToken(String token) throws AccessException {
    try {
      Jwts.parser().setSigningKey(key).parseClaimsJws(token);  // parser()로 변경
      return token;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT Token", e);
    } catch (ExpiredJwtException e) {
      log.info("ACCESS TOKEN이 만료되었습니다.");
      //return reissueService.reissueAccessToken(token);
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT Token", e);
    } catch (IllegalArgumentException e) {
      log.info("JWT claims string is empty.", e);
    }
    return null;
  }

  // 토큰에서 사용자 정보 가져오기
  public Claims getUserInfoFromToken(String token) {
    return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();  // parser()로 변경
  }
}
