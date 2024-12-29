package board.springboardpractice.controller;

import board.springboardpractice.dto.req.UserJoinRequest;
import board.springboardpractice.dto.req.UserLoginRequest;
import board.springboardpractice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final UserService userService;

  @GetMapping("/join")
  public String joinPage(Model model) {
    model.addAttribute("userJoinRequest", new UserJoinRequest());
    return "users/join";
  }

  @PostMapping("/join")
  public String join(@Valid @ModelAttribute UserJoinRequest req, Model model) {

    // Validation
    if (userService.join(req)) {
      return "users/join";
    }

    userService.join(req);
    model.addAttribute("message", "회원가입에 성공했습니다!\n로그인 후 사용 가능합니다!");
    model.addAttribute("nextUrl", "/users/login");
    return "printMessage";
  }

  @GetMapping("/login")
  public String loginPage(Model model, HttpServletRequest request) {

    // 로그인 성공 시 이전 페이지로 redirect 되게 하기 위해 세션에 저장
    String uri = request.getHeader("Referer");
    if (uri != null && !uri.contains("/login") && !uri.contains("/join")) {
      request.getSession().setAttribute("prevPage", uri);
    }

    model.addAttribute("userLoginRequest", new UserLoginRequest());
    return "users/login";
  }
}
