package board.springboardpractice.jwt;

import board.springboardpractice.dto.CustomUserDetails;
import board.springboardpractice.dto.req.UserLoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
      UserLoginRequest userLoginRequest = objectMapper.readValue(request.getInputStream(), UserLoginRequest.class);
      String loginId = userLoginRequest.getLoginId();
      String password = userLoginRequest.getPassword();

      log.info("loginId : {}", loginId);
      log.info("password : {}", password);

      return authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginId, password, null)
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨) => 인가
  @SneakyThrows
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
    log.info("성공!");
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

    String loginId = customUserDetails.getUsername(); //loginId 반환
    log.info("로그인ID : {}", loginId);

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();

    String role = auth.getAuthority();
    log.info("role used in jwt : {}", role);
    String token = jwtUtil.createJwt(loginId, role, expiredMs);

    log.info("token : {}", token);
    response.addHeader("Authorization", "Bearer " + token);
    String redirectUrl = "/" +  role + "/board";
    log.info("리다이렉트: {}", redirectUrl);
    try {
      log.info("성공");
      response.sendRedirect(redirectUrl);
    } catch (IOException e) {
      response.sendRedirect("/login");
    }
  }

  //로그인 실패시 실행하는 메소드
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    response.setStatus(401);

  }
}
