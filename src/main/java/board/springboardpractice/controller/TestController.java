package board.springboardpractice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class TestController {

  @GetMapping("/")
  public String mainP() {

    return "main Controller";
  }

  @GetMapping("/bronze/board")
  public String bronzeBoard() {

    return "bronzeBoard accessed!";
  }
}