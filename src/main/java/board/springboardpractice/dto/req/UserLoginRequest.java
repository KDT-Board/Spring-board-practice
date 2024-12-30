package board.springboardpractice.dto.req;

import lombok.Data;

@Data
public class UserLoginRequest {

  private String loginId;
  private String password;
}