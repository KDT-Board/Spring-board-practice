package board.springboardpractice.api.entity.user.dto.response;

import board.springboardpractice.api.entity.user.dto.request.SignUpRequest;
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
  public static SignUpResponse toSignUpResponse(SignUpRequest request){
    return SignUpResponse.builder()
            .username(request.username())
            .nickname(request.nickname())
            .email(request.email())
            .build();
  }
}
