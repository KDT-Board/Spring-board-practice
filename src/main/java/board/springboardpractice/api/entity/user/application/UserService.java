package board.springboardpractice.api.entity.user.application;

import board.springboardpractice.util.jwt.token.JwtToken;

public interface UserService {
  public JwtToken signIn(String username, String password);
}
