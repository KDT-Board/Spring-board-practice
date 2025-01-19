package board.springboardpractice.api.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserAddRequestDTO {
  @NotNull
  @NotEmpty
  private String userId;

  @NotNull
  @NotEmpty
  private String password;

  @NotNull
  @NotEmpty
  private String name;

  private String tel;

  private RoleName roleName;
}
