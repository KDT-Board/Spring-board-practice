package board.springboardpractice.api.entity.user.application;

import board.springboardpractice.api.entity.user.dto.request.SignUpRequest;
import board.springboardpractice.api.entity.user.dto.response.SignUpResponse;
import board.springboardpractice.util.jwt.token.JwtToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {
  JwtToken signIn(String username, String password);

  SignUpResponse save(SignUpRequest request, PasswordEncoder passwordEncoder);
}
