package board.springboardpractice.controller;

import board.springboardpractice.dto.req.UserJoinRequest;
import board.springboardpractice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
  private final UserService userService;
  private static final String LOGIN_PAGE = "login";
  private static final String JOIN_PAGE = "join";

  @PostMapping("/join")
  public String join(@RequestBody UserJoinRequest req) {
    log.info("req : ", req);
    userService.join(req);

    return "ok";
  }
}
