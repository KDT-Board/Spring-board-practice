package board.springboardpractice.api.entity.user.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {
  private RedisTemplate<String,String> redisTemplate;
  @Value("${jwt.refresh-expiration}")
  private long refreshTokenValidityTime;

  public void save(String refreshToken, String email) {
    redisTemplate.opsForValue().set(email, refreshToken, refreshTokenValidityTime, TimeUnit.SECONDS);
  }

  public Optional<String> findByEmail(final String email) {
    String refreshToken=(String)redisTemplate.opsForValue().get(email);
    return Optional.ofNullable(refreshToken);
  }
}
