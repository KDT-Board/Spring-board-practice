package board.springboardpractice.jwt;

import board.springboardpractice.dto.CustomUserDetails;
import board.springboardpractice.dto.req.UserLoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final Long expiredMs = 30 * 60 * 1000L;
  private final AuthenticationManager authenticationManager;
  private final JWTUtil jwtUtil;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)throws AuthenticationException {
    try {

      //JSON 파싱
      UserLoginRequest userLoginRequest = objectMapper.readValue(request.getInputStream(),
              UserLoginRequest.class);

      //클라이언트 요청에서 loginId, password 추출
      String loginId = userLoginRequest.getLoginId();
      String password = userLoginRequest.getPassword();

      log.info("loginId : {}", loginId);
      log.info("password : {}", password);

      //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password, null);

      //token에 담은 검증을 위한 AuthenticationManager로 전달
      return authenticationManager.authenticate(authToken);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨) => 인가
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
    log.info("성공!");
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

    String loginId = customUserDetails.getUsername(); //loginId 반환

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();

    String role = auth.getAuthority();
    String token = jwtUtil.createJwt(loginId, role, expiredMs);

    log.info("token : {}", token);
    response.addHeader("Authorization", "Bearer " + token);

  }

  //로그인 실패시 실행하는 메소드
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    response.setStatus(401);

  }
}
