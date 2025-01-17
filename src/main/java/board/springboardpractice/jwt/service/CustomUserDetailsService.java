package board.springboardpractice.jwt.service;

import board.springboardpractice.domain.user.User;
import board.springboardpractice.global.exception.BusinessException;
import board.springboardpractice.jwt.UserPrincipal;
import board.springboardpractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static board.springboardpractice.global.exception.BusinessExceptionStatus.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;
  @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

    User user = null;
    try {
      user = userRepository.findById(Long.valueOf(loginId))
              .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    } catch (BusinessException e) {
      throw new RuntimeException(e);
    }

    return new UserPrincipal(user);

  }
}
