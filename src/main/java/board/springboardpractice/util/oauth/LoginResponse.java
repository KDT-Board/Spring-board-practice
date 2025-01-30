package board.springboardpractice.util.oauth;

import board.springboardpractice.util.jwt.token.JwtToken;
import lombok.Builder;

public record LoginResponse(
        String nickname,
        String email,
        AuthTokens token
) {
  @Builder
  public LoginResponse(String nickname, String email, AuthTokens token) {
    this.nickname = nickname;
    this.email = email;
    this.token = token;
  }
}
