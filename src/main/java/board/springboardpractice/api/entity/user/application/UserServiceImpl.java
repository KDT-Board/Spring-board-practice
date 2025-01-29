package board.springboardpractice.api.entity.user.application;

import board.springboardpractice.api.entity.user.User;
import board.springboardpractice.api.entity.user.dto.request.SignUpRequest;
import board.springboardpractice.api.entity.user.dto.response.SignUpResponse;
import board.springboardpractice.api.entity.user.infrastructure.UserRepository;
import board.springboardpractice.util.jwt.token.JwtToken;
import board.springboardpractice.util.jwt.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService{
  private final UserRepository userRepository;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  
  //회원가입으로 들어온 요청 처리
  @Transactional
  @Override
  public SignUpResponse save(SignUpRequest request, PasswordEncoder passwordEncoder) {
    User saved = userRepository.save(User.toEntity(request,passwordEncoder));
    log.info("saved User: {}", saved.getEmail());
    return SignUpResponse.toSignUpResponse(saved);
  }

  //로그인 요청으로 들어온 username + password 기반으로 Authentication객체 생성
  @Transactional
  @Override
  public JwtToken signIn(String username, String password) {
    // 1. username + password 를 기반으로 Authentication 객체 생성
    // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

    // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
    // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    // 3. 인증 정보를 기반으로 JWT 토큰 생성
    JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);

    return jwtToken;
  }
}
