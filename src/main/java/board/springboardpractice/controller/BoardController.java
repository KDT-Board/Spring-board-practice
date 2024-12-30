package board.springboardpractice.controller;

import board.springboardpractice.jwt.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@ResponseBody
public class TestController {

  @GetMapping("/")
  public String mainP() {

    return "main Controller";
  }

  @GetMapping("/bronze/board")
  public String bronzeBoard() {

    String loginId = SecurityUtil.getCurrentUserLoginId();
    log.info("현재 /bronze/board에 로그인한 사용자 : {}" , loginId);

    return "bronzeBoard accessed!";
  }
}