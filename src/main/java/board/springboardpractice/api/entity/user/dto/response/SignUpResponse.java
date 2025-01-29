package board.springboardpractice.api.entity.user.dto.response;

import board.springboardpractice.api.entity.user.User;
import lombok.Builder;

public record SignUpResponse(
        String username,
        String nickname,
        String email
) {
  @Builder
  public SignUpResponse(String username, String nickname, String email) {
    this.username = username;
    this.nickname = nickname;
    this.email = email;
  }

  //request to SignUpResponse
  public static SignUpResponse toSignUpResponse(User user){
    return SignUpResponse.builder()
            .username(user.getUsername())
            .nickname(user.getNickname())
            .email(user.getEmail())
            .build();
  }
}
