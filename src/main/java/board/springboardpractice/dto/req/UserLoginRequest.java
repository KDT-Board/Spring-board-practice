package board.springboardpractice.dto.req;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserLoginRequest {
  @NonNull
  private String loginId;
  @NonNull
  private String password;
}