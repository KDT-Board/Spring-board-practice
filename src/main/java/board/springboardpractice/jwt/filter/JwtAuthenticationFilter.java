package board.springboardpractice.jwt.filter;

import board.springboardpractice.domain.user.User;
import board.springboardpractice.jwt.JwtRule;
import board.springboardpractice.jwt.service.JwtService;
import board.springboardpractice.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {//JWT 발급 이후, API를 요청할 때마다 처리하는 부분
  private final JwtService jwtService;
  private final UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {

    if (isPermittedURI(request.getRequestURI())) {
      SecurityContextHolder.getContext().setAuthentication(null);
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = jwtService.resolveTokenFromCookie(request, JwtRule.ACCESS_PREFIX);
    if (jwtService.validateAccessToken(accessToken)) {
      setAuthenticationToContext(accessToken);
      filterChain.doFilter(request, response);
      return;
    }

    String refreshToken = jwtService.resolveTokenFromCookie(request, JwtRule.REFRESH_PREFIX);
    User user = findUserByRefreshToken(refreshToken);

    if (jwtService.validateRefreshToken(refreshToken, user.getIdentifier())) {
      String reissuedAccessToken = jwtService.generateAccessToken(response, user);
      jwtService.generateRefreshToken(response, user);

      setAuthenticationToContext(reissuedAccessToken);
      filterChain.doFilter(request, response);
      return;
    }

    jwtService.logout(user, response);
  }

  private boolean isPermittedURI(String requestURI) {
    return Arrays.stream(PERMITTED_URI)
            .anyMatch(permitted -> {
              String replace = permitted.replace("*", "");
              return requestURI.contains(replace) || replace.contains(requestURI);
            });
  }

  private User findUserByRefreshToken(String refreshToken) {
    String identifier = jwtService.getIdentifierFromRefresh(refreshToken);
    return userService.findUserByIdentifier(identifier);
  }

  private void setAuthenticationToContext(String accessToken) {
    Authentication authentication = jwtService.getAuthentication(accessToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}