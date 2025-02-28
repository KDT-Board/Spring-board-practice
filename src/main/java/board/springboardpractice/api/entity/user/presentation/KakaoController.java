package board.springboardpractice.api.entity.user.presentation;

import board.springboardpractice.api.common.response.entity.ApiResponseEntity;
import board.springboardpractice.api.entity.user.application.KakaoService;
import board.springboardpractice.util.oauth.LoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KakaoController {
  private final KakaoService kakaoService;

  private static final String KAKAO_AUTH_URI = "https://kauth.kakao.com/oauth/authorize";

  @Value("${oauth2.kakao.api_key}")
  private String clientId;

  @Value("${oauth2.kakao.redirect_uri}")
  private String redirectUri;

  @GetMapping("/kakaologin")
  public ModelAndView kakaoLogin() {
    ModelAndView mav = new ModelAndView();
    StringBuilder uri = new StringBuilder();
    uri.append(KAKAO_AUTH_URI)
            .append("?response_type=code")
            .append("&client_id=").append(clientId)
            .append("&redirect_uri=").append(redirectUri);

    mav.setViewName("redirect:" + uri);
    return mav;
  }

  @GetMapping("/callback")
  public ResponseEntity<ApiResponseEntity> kakaoLoginRedirect(@RequestParam(value = "code", required = false) String code) throws Throwable {
    System.out.println("code:" + code);
    LoginResponse loginResponse = kakaoService.kakaoLogin(code);
    log.info("{} : ", loginResponse.token());
    return ApiResponseEntity.successResponseEntity(
            loginResponse
    );
  }
}
