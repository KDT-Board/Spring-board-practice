package board.springboardpractice.api.entity.user.application;

import board.springboardpractice.util.oauth.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class KakaoService {

  @Value("${oauth2.kakao.api_key}")
  private String clientId;

  @Value("${oauth2.kakao.redirect_uri}")
  private String redirectUri;

  public LoginResponse kakaoLogin(String code) {
    //1. "인가 코드"로 "액세스 토큰" 요청
    String accessToken = getAccessToken(code);
    //2. 토큰으로 카카오 API 호출
    HashMap<String, Object> userInfo= getKakaoUserInfo(accessToken);
    //3. 카카오ID로 회원가입 & 로그인 처리
    LoginResponse kakaoUserResponse= kakaoUserLogin(userInfo);

    return kakaoUserResponse;
  }

  //1. "인가 코드"로 "액세스 토큰" 요청
  private String getAccessToken(String code) {

    // HTTP Header 생성
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    // HTTP Body 생성
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", clientId);
    body.add("redirect_uri", redirectUri);
    body.add("code", code);

    // HTTP 요청 보내기
    HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
    RestTemplate rt = new RestTemplate();
    ResponseEntity<String> response = rt.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
    );

    // HTTP 응답 (JSON) -> 액세스 토큰 파싱
    String responseBody = response.getBody();
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = null;
    try {
      jsonNode = objectMapper.readTree(responseBody);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return jsonNode.get("access_token").asText(); //토큰 전송
  }
}
