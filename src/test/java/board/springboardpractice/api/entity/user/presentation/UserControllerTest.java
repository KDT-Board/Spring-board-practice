package board.springboardpractice.api.entity.user.presentation;

import board.springboardpractice.api.entity.user.application.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  protected UserService userService;
  @Autowired
  protected PasswordEncoder passwordEncoder;

  //회원가입
  @Test
  public void signUp(){
    //given

  }

  //로그인
  public void signIn(){

  }
}