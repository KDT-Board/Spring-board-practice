package board.springboardpractice.api.entity.user.dto.request;

import lombok.Builder;

public record SignUpRequest(
        String username,
        String password,
        String nickname,
        String email,
        String profileImg
) {
  @Builder
  public SignUpRequest(String username, String password, String nickname, String email, String profileImg) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.email = email;
    this.profileImg = profileImg;
  }
}
