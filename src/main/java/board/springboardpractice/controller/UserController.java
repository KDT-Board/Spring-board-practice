package board.springboardpractice.controller;

import board.springboardpractice.dto.req.UserJoinRequest;
import board.springboardpractice.dto.req.UserLoginRequest;
import board.springboardpractice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
  private final UserService userService;
  private static final String LOGIN_PAGE = "users/login";
  private static final String JOIN_PAGE = "users/join";

  @GetMapping("/join")
  public String joinPage(Model model) {
    model.addAttribute("userJoinRequest", new UserJoinRequest());
    return JOIN_PAGE;
  }

  @PostMapping("/join")
  public String join(@Valid @ModelAttribute UserJoinRequest req,
                     BindingResult bindingResult,
                     Model model) {
    // 유효성 검증 실패 시
    if (bindingResult.hasErrors()) {
      return JOIN_PAGE;
    }

    try {
      userService.join(req);
      model.addAttribute("message", "회원가입에 성공했습니다!\n로그인 후 사용 가능합니다!");
      model.addAttribute("nextUrl", "/users/login");
      return "printMessage";
    } catch (Exception e) {
      model.addAttribute("errorMessage", e.getMessage());
      return JOIN_PAGE;
    }
  }

  @GetMapping("/login")
  public String loginPage(Model model, HttpServletRequest request) {
    // 이미 로그인된 사용자 처리
    if (request.getSession().getAttribute("user") != null) {
      return "redirect:/";
    }

    model.addAttribute("userLoginRequest", new UserLoginRequest());
    return LOGIN_PAGE;
  }

  // 로그인 실패 처리 추가
  @GetMapping("/login-error")
  public String loginError(Model model) {
    model.addAttribute("loginError", true);
    model.addAttribute("userLoginRequest", new UserLoginRequest());
    return LOGIN_PAGE;
  }
}
