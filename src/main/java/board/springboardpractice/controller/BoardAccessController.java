package board.springboardpractice.controller;

import board.springboardpractice.jwt.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class BoardAccessController {
  private static final String BRONZE_PAGE = "/member/BRONZE/board";

  @GetMapping("/BRONZE/board")
  public String bronzeBoard() {

    String loginId = SecurityUtil.getCurrentUserLoginId();
    log.info("현재 /BRONZE/board에 로그인한 사용자 : {}" , loginId);

    return BRONZE_PAGE;
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