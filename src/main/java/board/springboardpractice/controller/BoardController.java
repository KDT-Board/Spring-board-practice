package board.springboardpractice.controller;

import board.springboardpractice.jwt.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@ResponseBody
public class BoardController {

  @GetMapping("/bronze/board")
  public String bronzeBoard() {

    String loginId = SecurityUtil.getCurrentUserLoginId();
    log.info("현재 /bronze/board에 로그인한 사용자 : {}" , loginId);

    return "bronzeBoard accessed!";
  }

  @GetMapping("/silver/board")
  public String silverBoard() {

    String loginId = SecurityUtil.getCurrentUserLoginId();
    log.info("현재 /silver/board에 로그인한 사용자 : {}" , loginId);

    return "silverBoard accessed!";
  }


  @GetMapping("/gold/board")
  public String goldBoard() {

    String loginId = SecurityUtil.getCurrentUserLoginId();
    log.info("현재 /gold/board에 로그인한 사용자 : {}" , loginId);

    return "goldBoard accessed!";
  }


  @GetMapping("/admin/board")
  public String adminBoard() {

    String loginId = SecurityUtil.getCurrentUserLoginId();
    log.info("현재 /admin/board에 로그인한 사용자 : {}" , loginId);

    return "adminBoard accessed!";
  }
}