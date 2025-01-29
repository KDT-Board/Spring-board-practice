package board.springboardpractice.api.entity.user.presentation;

import board.springboardpractice.api.entity.user.application.UserService;
import board.springboardpractice.api.entity.user.dto.SignInRequest;
import board.springboardpractice.util.jwt.token.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  //회원가입
//  @PostMapping("/sign-up")
//  public

  //로그인
  @PostMapping("/sign-in")
  public JwtToken signIn(@RequestBody SignInRequest request){
    String username = request.username();
    String password = request.password();
    JwtToken jwtToken = userService.signIn(username, password);
    log.info("request username = {}, password = {}", username, password);
    log.info("JwtToken accesstoken = {}, refreshtoken ={}",
            jwtToken.accessToken(), jwtToken.refreshToken());

    return jwtToken;
  }

  //USER 권한을 가진 사용자에게 허용
  @PostMapping("/test")
  public String test(){
    return "success";
  }
}
