package board.springboardpractice.jwt;

import board.springboardpractice.domain.User;
import board.springboardpractice.dto.CustomUserDetails;
import board.springboardpractice.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
  인증
 */
@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
  private final JWTUtil jwtUtil;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String token = null;

    // 1. 헤더에서 토큰 확인
    String authorization = request.getHeader("Authorization");
    if (authorization != null && authorization.startsWith("Bearer ")) {
      token = authorization.split(" ")[1];
    }

    // 2. 헤더에 없다면 쿠키에서 토큰 확인
    if (token == null) {
      Cookie[] cookies = request.getCookies();
      if (cookies != null) {
        for (Cookie cookie : cookies) {
          if ("Authorization".equals(cookie.getName())) {
            token = cookie.getValue();
            break;
          }
        }
      }
    }

    // 3. 토큰이 없으면 다음 필터로
    if (token == null) {
      log.info("token is null");
      filterChain.doFilter(request, response);
      return;
    }

    // 4. 토큰 만료 여부 검증
    if (jwtUtil.isExpired(token)) {
      log.info("token 검증 : token is expired");
      filterChain.doFilter(request, response);
      return;
    }

    // 5. 토큰에서 사용자 정보 추출
    String loginId = jwtUtil.getLoginId(token);
    User user = userRepository.findByLoginId(loginId)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    // 6. UserDetails에 회원 정보 객체 담기
    CustomUserDetails customUserDetails = new CustomUserDetails(user);

    log.info("JWTFilter customerDetails loginId : {}", customUserDetails.getUsername());

    // 7. 스프링 시큐리티 인증 토큰 생성
    Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

    // 8. 세션에 사용자 등록
    SecurityContextHolder.getContext().setAuthentication(authToken);

    // 9. 경로가 /login인 경우 리다이렉트
    String requestURI = request.getRequestURI();
    if ("/login".equals(requestURI)) {
      String role = user.getUserRole().getValue(); // 사용자의 역할 가져오기
      response.sendRedirect("/" + role + "/board");
      return; // 필터 체인 진행하지 않고 종료
    }

    filterChain.doFilter(request, response);
  }
}