package board.springboardpractice.util.oauth;

import lombok.Builder;

public record AuthTokens(
        String accessToken,
        String refreshToken,
        String grantType,
        Long expiresIn
) {

  @Builder
  public AuthTokens(String accessToken, String refreshToken, String grantType, Long expiresIn) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.grantType = grantType;
    this.expiresIn = expiresIn;
  }

  public static AuthTokens of(String accessToken, String refreshToken, String grantType, Long expiresIn) {
    return AuthTokens.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .grantType(grantType)
            .expiresIn(expiresIn)
            .build();
  }
}