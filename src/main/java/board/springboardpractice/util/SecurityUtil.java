package board.springboardpractice.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {
  public static String getCurrentUsername(){
    final Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getName() == null){
      throw new RuntimeException("No authentication information");
    }
    return authentication.getName();
  }
}
