package board.springboardpractice.config.redis.application;

import board.springboardpractice.config.redis.entity.RefreshToken;
import board.springboardpractice.config.redis.infrastructure.RefreshTokenRepository;
import board.springboardpractice.util.jwt.token.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;

  @Transactional
  public void saveTokenInfo(String username, JwtToken tokens) {
    refreshTokenRepository.save(RefreshToken.builder()
            .username(username)
            .accessToken(tokens.accessToken())
            .refreshToken(tokens.refreshToken())
            .build()
    );
  }

  @Transactional(readOnly = true)
  public RefreshToken getTokenInfoByUsername(String username){

    return refreshTokenRepository.findById(username)
            .orElseThrow(() -> new RuntimeException("없다!"));
  }

  @Transactional
  public void removeRefreshToken(String accessToken){
    refreshTokenRepository.findByAccessToken(accessToken)
            .ifPresent(refreshTokenRepository::delete);
  }
}