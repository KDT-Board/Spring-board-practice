package board.springboardpractice.service;

import board.springboardpractice.domain.User;
import board.springboardpractice.dto.req.UserJoinRequest;
import board.springboardpractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder encoder;

  /**
   * 회원가입하는 함수
   * @param req
   */
  public boolean join(UserJoinRequest req) {
    if (isUserExists(req.getLoginId(), req.getNickname())) {
      return false; // 이미 존재하는 사용자면 false 반환
    }

    String encodedPassword = encoder.encode(req.getPassword());
    userRepository.save(req.toEntity(encodedPassword));
    return true;
  }

  /**
   * 사용자 존재 여부 확인
   * @param loginId
   * @return
   */
  private boolean isUserExists(String loginId,String nickname) {
    return userRepository.existsByLoginId(loginId) && userRepository.existsByNickname(nickname);
  }

  /**
   * 유저 정보
   *
   * @param loginId
   * @return
   */
  public User userInfo(String loginId) {
    return userRepository.findByLoginId(loginId).get();
  }


}
