package board.springboardpractice.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;

@Service
@Transactional(readOnly = true)
@Slf4j
public class JwtService {
  private final CustomUserDetailsService customUserDetailsService;
  private final JwtGenerator jwtGenerator;
  private final JwtUtil jwtUtil;
  private final TokenRepository tokenRepository;

  private final Key ACCESS_SECRET_KEY;
  private final Key REFRESH_SECRET_KEY;
  private final long ACCESS_EXPIRATION;
  private final long REFRESH_EXPIRATION;

  public JwtService(CustomUserDetailsService customUserDetailsService, JwtGenerator jwtGenerator,
                    JwtUtil jwtUtil, TokenRepository tokenRepository,
                    @Value("${jwt.access-secret}") String ACCESS_SECRET_KEY,
                    @Value("${jwt.refresh-secret}") String REFRESH_SECRET_KEY,
                    @Value("${jwt.access-expiration}") long ACCESS_EXPIRATION,
                    @Value("${jwt.refresh-expiration}") long REFRESH_EXPIRATION) {
    this.customUserDetailsService = customUserDetailsService;
    this.jwtGenerator = jwtGenerator;
    this.jwtUtil = jwtUtil;
    this.tokenRepository = tokenRepository;
    this.ACCESS_SECRET_KEY = jwtUtil.getSigningKey(ACCESS_SECRET_KEY);
    this.REFRESH_SECRET_KEY = jwtUtil.getSigningKey(REFRESH_SECRET_KEY);
    this.ACCESS_EXPIRATION = ACCESS_EXPIRATION;
    this.REFRESH_EXPIRATION = REFRESH_EXPIRATION;
  }
}
