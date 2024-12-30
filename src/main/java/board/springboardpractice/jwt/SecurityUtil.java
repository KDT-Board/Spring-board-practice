package board.springboardpractice.jwt;

import board.springboardpractice.dto.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SecurityUtil {

  // 현재 인증된 사용자의 CustomUserDetails 가져오기
  public static CustomUserDetails getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UsernameNotFoundException("인증된 사용자를 찾을 수 없습니다.");
    }

    Object principal = authentication.getPrincipal();

    if (!(principal instanceof CustomUserDetails)) {
      throw new RuntimeException("CustomUserDetails 타입이 아닙니다.");
    }

    return (CustomUserDetails) principal;
  }

  // 현재 인증된 사용자의 loginId 가져오기
  public static String getCurrentUserLoginId() {
    return getCurrentUser().getUsername();
  }
}