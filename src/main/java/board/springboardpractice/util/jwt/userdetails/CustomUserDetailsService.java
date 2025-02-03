package board.springboardpractice.util.jwt.userdetails;

import board.springboardpractice.api.entity.global.Level;
import board.springboardpractice.api.entity.user.domain.User;
import board.springboardpractice.api.entity.user.infrastructure.UserRepository;
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
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username)
            .map(this::createUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다."));
  }

  // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
  private UserDetails createUserDetails(User user) {
    return User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRoles())
            .build();
  }

  public void validateAvailableUser(UserDetails userDetails, Level currentLevel) {
    boolean hasRequiredLevel = userDetails.getAuthorities().stream()
            .anyMatch(authority -> {
              try {
                Level level = Level.valueOf(String.valueOf(authority));
                return level.ordinal() >= currentLevel.ordinal();
              } catch (IllegalArgumentException e) {
                return false;
              }
            });

    if (!hasRequiredLevel) {
      throw new IllegalArgumentException("Required " + currentLevel +" level or higher");
    }
  }
}
