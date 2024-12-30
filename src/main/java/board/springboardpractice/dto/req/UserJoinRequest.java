package board.springboardpractice.dto.req;

import board.springboardpractice.domain.User;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserJoinRequest {
  @NonNull
  private String loginId;
  @NonNull
  @Min(8)
  private String password;
  @NonNull
  private String nickname;

  public User toEntity(String encodedPassword) {
    return User.of(
            this.loginId,
            encodedPassword,  //암호화된 비밀번호 사용
            this.nickname
    );
  }

}
