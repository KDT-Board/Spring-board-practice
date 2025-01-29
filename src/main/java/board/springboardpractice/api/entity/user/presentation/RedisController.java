package board.springboardpractice.api.entity.user.presentation;

import board.springboardpractice.config.redis.application.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/redis")
public class RedisController {
  private final RefreshTokenService refreshTokenService;

  //reissue
  @PostMapping("")
}
