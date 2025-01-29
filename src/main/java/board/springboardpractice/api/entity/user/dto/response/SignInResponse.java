package board.springboardpractice.api.entity.user.dto.response;

import board.springboardpractice.util.jwt.token.JwtToken;

public record SignInResponse(
  String username,
  JwtToken jwtToken
) {
}
