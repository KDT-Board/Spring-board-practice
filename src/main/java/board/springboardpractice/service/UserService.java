package board.springboardpractice.service;

import board.springboardpractice.domain.User;
import board.springboardpractice.dto.req.UserJoinRequest;
import board.springboardpractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder encoder;

  /**
   * 회원가입하는 함수
   * @param req
   */
  public boolean join(UserJoinRequest req){
    userRepository.save(req.toEntity( encoder.encode(req.getPassword()) ));
    return true;
  }

  /**
   * 유저 정보
   * @param loginId
   * @return
   */
  public User userInfo(String loginId) {
    return userRepository.findByLoginId(loginId).get();
  }




}
