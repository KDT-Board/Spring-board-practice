package board.springboardpractice.service;

import board.springboardpractice.domain.User;
import board.springboardpractice.dto.CustomUserDetails;
import board.springboardpractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;
  @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

    User user= userRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new UsernameNotFoundException("등록된 사용자가 아닙니다: " + loginId));
    return new CustomUserDetails(user);

  }
}
