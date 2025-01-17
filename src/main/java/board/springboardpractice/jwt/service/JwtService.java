package board.springboardpractice.jwt.service;

import board.springboardpractice.domain.user.Role;
import board.springboardpractice.domain.user.User;
import board.springboardpractice.global.exception.BusinessException;
import board.springboardpractice.jwt.JwtRule;
import board.springboardpractice.jwt.TokenStatus;
import board.springboardpractice.jwt.util.JwtGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import board.springboardpractice.jwt.repository.TokenRepository;
import board.springboardpractice.jwt.util.JWTUtil;

import java.security.Key;

import static board.springboardpractice.global.exception.BusinessExceptionStatus.JWT_TOKEN_NOT_FOUND;
import static board.springboardpractice.jwt.JwtRule.*;

@Service
@Transactional(readOnly = true)
@Slf4j
public class JwtService {
  private final CustomUserDetailsService customUserDetailsService;
  private final JwtGenerator jwtGenerator;
  private final JWTUtil jwtUtil;
  private final TokenRepository tokenRepository;

  private final Key ACCESS_SECRET_KEY;
  private final Key REFRESH_SECRET_KEY;
  private final long ACCESS_EXPIRATION;
  private final long REFRESH_EXPIRATION;

  public JwtService(CustomUserDetailsService customUserDetailsService, JwtGenerator jwtGenerator,
                    JWTUtil jwtUtil, TokenRepository tokenRepository,
                    @Value("${jwt.access-secret}") String ACCESS_SECRET_KEY,
                    @Value("${jwt.refresh-secret}") String REFRESH_SECRET_KEY,
                    @Value("${jwt.access-expiration}") long ACCESS_EXPIRATION,
                    @Value("${jwt.refresh-expiration}") long REFRESH_EXPIRATION) {
    this.customUserDetailsService = customUserDetailsService;
    this.jwtGenerator = jwtGenerator;
    this.jwtUtil = jwtUtil;
    this.tokenRepository = tokenRepository;
    this.ACCESS_SECRET_KEY = jwtUtil.verifyWith(ACCESS_SECRET_KEY);
    this.REFRESH_SECRET_KEY = jwtUtil.verifyWith(REFRESH_SECRET_KEY);
    this.ACCESS_EXPIRATION = ACCESS_EXPIRATION;
    this.REFRESH_EXPIRATION = REFRESH_EXPIRATION;
  }

  public void validateUser(User requestUser) {
    if (requestUser.getRole() == Role.NOT_REGISTERED) {
      throw new BusinessException(NOT_AUTHENTICATED_USER);
    }
  }

  public String generateAccessToken(HttpServletResponse response, User requestUser) {
    String accessToken = jwtGenerator.generateAccessToken(ACCESS_SECRET_KEY, ACCESS_EXPIRATION, requestUser);
    ResponseCookie cookie = setTokenToCookie(ACCESS_PREFIX.getValue(), accessToken, ACCESS_EXPIRATION / 1000);
    response.addHeader(JWT_ISSUE_HEADER.getValue(), cookie.toString());

    return accessToken;
  }

  @Transactional
  public String generateRefreshToken(HttpServletResponse response, User requestUser) {
    String refreshToken = jwtGenerator.generateRefreshToken(REFRESH_SECRET_KEY, REFRESH_EXPIRATION, requestUser);
    ResponseCookie cookie = setTokenToCookie(REFRESH_PREFIX.getValue(), refreshToken, REFRESH_EXPIRATION / 1000);
    response.addHeader(JWT_ISSUE_HEADER.getValue(), cookie.toString());

    tokenRepository.save(new Token(requestUser.getIdentifier(), refreshToken));
    return refreshToken;
  }

  private ResponseCookie setTokenToCookie(String tokenPrefix, String token, long maxAgeSeconds) {
    return ResponseCookie.from(tokenPrefix, token)
            .path("/")
            .maxAge(maxAgeSeconds)
            .httpOnly(true)
            .sameSite("Lax")
            .secure(true)
            .build();
  }

  public boolean validateAccessToken(String token) {
    return jwtUtil.getTokenStatus(token, ACCESS_SECRET_KEY) == TokenStatus.AUTHENTICATED;
  }

  public boolean validateRefreshToken(String token, String identifier) {
    boolean isRefreshValid = jwtUtil.getTokenStatus(token, REFRESH_SECRET_KEY) == TokenStatus.AUTHENTICATED;

    Token storedToken = tokenRepository.findByIdentifier(identifier);
    boolean isTokenMatched = storedToken.getToken().equals(token);

    return isRefreshValid && isTokenMatched;
  }

  public boolean validateRefreshToken(String token, String identifier) {
    boolean isRefreshValid = jwtUtil.getTokenStatus(token, REFRESH_SECRET_KEY) == TokenStatus.AUTHENTICATED;

    Token storedToken = tokenRepository.findByIdentifier(identifier);
    boolean isTokenMatched = storedToken.getToken().equals(token);

    return isRefreshValid && isTokenMatched;
  }

  public String resolveTokenFromCookie(HttpServletRequest request, JwtRule tokenPrefix) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      throw new BusinessException(JWT_TOKEN_NOT_FOUND);
    }
    return jwtUtil.resolveTokenFromCookie(cookies, tokenPrefix);
  }

  public Authentication getAuthentication(String token) {
    UserDetails principal = customUserDetailsService.loadUserByUsername(getUserPk(token, ACCESS_SECRET_KEY));
    return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
  }

  private String getUserPk(String token, Key secretKey) {
    return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }

  public String getIdentifierFromRefresh(String refreshToken) {
    try {
      return Jwts.parserBuilder()
              .setSigningKey(REFRESH_SECRET_KEY)
              .build()
              .parseClaimsJws(refreshToken)
              .getBody()
              .getSubject();
    } catch (Exception e) {
      throw new BusinessException(ErrorCode.INVALID_JWT);
    }
  }

  public void logout(User requestUser, HttpServletResponse response) {
    tokenRepository.deleteById(requestUser.getIdentifier());

    Cookie accessCookie = jwtUtil.resetToken(ACCESS_PREFIX);
    Cookie refreshCookie = jwtUtil.resetToken(REFRESH_PREFIX);

    response.addCookie(accessCookie);
    response.addCookie(refreshCookie);
  }
}
