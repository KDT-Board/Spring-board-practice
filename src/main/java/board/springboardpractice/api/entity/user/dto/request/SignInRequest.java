package board.springboardpractice.api.entity.user.dto.request;


import lombok.Builder;

public record SignInRequest(
       String username,
       String password
) {
  @Builder
  public SignInRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }
}
