package board.springboardpractice.controller;

import board.springboardpractice.dto.req.UserJoinRequest;
import board.springboardpractice.dto.req.UserLoginRequest;
import board.springboardpractice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
  private final UserService userService;
  private static final String LOGIN_PAGE = "login";
  private static final String JOIN_PAGE = "join";

  @GetMapping("/login")
  public String loginPage() {
    return LOGIN_PAGE;
  }

  @GetMapping("/join")
  public String joinPage() {
    return JOIN_PAGE;
  }

  @PostMapping("/join")
  public String join(@RequestBody UserJoinRequest req) {
    log.info("req : ", req);
    userService.join(req);

    return "ok";
  }

  @PostMapping("/login")
  public String login(@RequestBody UserLoginRequest req) {
    // LoginFilter에서 실제 인증이 처리되므로,
    // 이 메서드는 LoginFilter를 통과하지 못한 요청에 대한 처리만 담당
    return "fail";
  }
}
