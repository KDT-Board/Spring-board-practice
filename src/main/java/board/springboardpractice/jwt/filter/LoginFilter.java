package board.springboardpractice.jwt.filter;

import board.springboardpractice.dto.req.UserLoginRequest;
import board.springboardpractice.jwt.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
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
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final Long expiredMs = 30 * 60 * 1000L;
  private final AuthenticationManager authenticationManager;
  private final JWTUtil jwtUtil;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      UserLoginRequest userLoginRequest = objectMapper.readValue(request.getInputStream(), UserLoginRequest.class);
      String loginId = userLoginRequest.getLoginId();
      String password = userLoginRequest.getPassword();

      log.info("Attempting authentication for loginId: {}", loginId);

      return authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginId, password)
      );
    } catch (IOException e) {
      throw new RuntimeException("Failed to parse login request", e);
    }
  }

  @SneakyThrows
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
    log.info("Authentication successful!");
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

    String loginId = customUserDetails.getUsername();
    log.info("Authenticated user loginId: {}", loginId);

    // Extract roles and create JWT
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    String role = authorities.stream().findFirst().map(GrantedAuthority::getAuthority).orElse("USER");
    log.info("Role used in JWT: {}", role);

    String token = jwtUtil.createJwt(loginId, role, expiredMs); //로그인 후 유효 토큰 생성
    Cookie cookie = new Cookie("Authorization", token);
    cookie.setHttpOnly(true);  // XSS 방지
    cookie.setPath("/");       // 모든 경로에서 접근 가능
    cookie.setMaxAge((int) (expiredMs / 1000));  // 쿠키 만료시간 설정
    response.addCookie(cookie);

    String redirectUrl = "/" + role + "/board";
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    objectMapper.writeValue(response.getWriter(), Map.of(
            "token" , token,
            "role", role,
            "redirectUrl", redirectUrl,
            "message", "Login successful"
    ));

    log.info("Login success");
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    log.error("Authentication failed: {}", failed.getMessage());
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    try {
      response.getWriter().write(objectMapper.writeValueAsString(Map.of(
              "error", "Authentication failed",
              "message", failed.getMessage()
      )));
    } catch (IOException e) {
      log.error("Failed to write failure response", e);
    }
  }
}
