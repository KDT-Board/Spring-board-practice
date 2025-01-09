package board.springboardpractice.controller;

import board.springboardpractice.dto.req.UserJoinRequest;
import board.springboardpractice.dto.req.UserLoginRequest;
import board.springboardpractice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private static final String LOGIN_PAGE = "/member/login";
  private static final String JOIN_PAGE = "/member/join";

  @GetMapping("/login")
  public String loginPage(Model model) {
    model.addAttribute("member", new UserLoginRequest());
    return LOGIN_PAGE;
  }

  @GetMapping("/join")
  public String joinPage(Model model) {
    model.addAttribute("member", new UserJoinRequest());
    return JOIN_PAGE;
  }

  @PostMapping("/join")
  public String join(@ModelAttribute("member") UserJoinRequest req, Model model) {
    model.addAttribute("member", req);
    log.info("Join request - loginId: {}", req.getLoginId());
    log.info("Join request - password: {}", req.getPassword());
    log.info("Join request - nickname: {}", req.getNickname());

    userService.join(req); // userService에서 회원가입 로직 실행
    return "redirect:/login";
  }


  @PostMapping("/login")
  public String login(@ModelAttribute("member") UserLoginRequest req, Model model) {
    model.addAttribute("member", req);
    // LoginFilter에서 실제 인증이 처리되므로,
    // 이 메서드는 LoginFilter를 통과하지 못한 요청에 대한 처리만 담당
    return LOGIN_PAGE;
  }
}
