package board.springboardpractice.api.entity.user.presentation;

import board.springboardpractice.api.common.response.entity.ApiResponseEntity;
import board.springboardpractice.api.entity.user.application.UserService;
import board.springboardpractice.api.entity.user.dto.request.SignInRequest;
import board.springboardpractice.api.entity.user.dto.request.SignUpRequest;
import board.springboardpractice.api.entity.user.dto.response.SignInResponse;
import board.springboardpractice.api.entity.user.dto.response.SignUpResponse;
import board.springboardpractice.util.SecurityUtil;
import board.springboardpractice.util.jwt.token.JwtToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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
  private final PasswordEncoder passwordEncoder;

  //회원가입
  @PostMapping("/sign-up")
  public ResponseEntity<ApiResponseEntity> signUp(@Valid @RequestBody SignUpRequest request){
    SignUpResponse signUpResponse = userService.save(request,passwordEncoder);

    return ApiResponseEntity.successResponseEntity(
            signUpResponse
    );
  }

  //로그인
  @PostMapping("/sign-in")
  public ResponseEntity<ApiResponseEntity> signIn(@Valid @RequestBody SignInRequest request){
    String username = request.username();
    String password = request.password();

    JwtToken jwtToken = userService.signIn(username, password);
    log.info("JwtToken accesstoken = {}, refreshtoken ={}",
            jwtToken.accessToken(), jwtToken.refreshToken());

    SignInResponse signInResponse = new SignInResponse(username, jwtToken);

    return ApiResponseEntity.successResponseEntity(
            signInResponse
    );
  }

  /**
   * 간단 테스트
   */
  //USER 권한을 가진 사용자에게 허용
  @PostMapping("/test")
  public String test(){
    return "success";
  }

  //어떤 회원이 api 요청했는지 조회
  @PostMapping("/test/who")
  public String testWho(){
    return SecurityUtil.getCurrentUsername();
  }
}
