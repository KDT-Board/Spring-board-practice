package board.springboardpractice.controller;

import board.springboardpractice.dto.CustomUserDetails;
import board.springboardpractice.dto.req.BoardRequestDto;
import board.springboardpractice.dto.req.UserJoinRequest;
import board.springboardpractice.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/BRONZE/board") //추후 다른 레벨의 보드에서도 적용되도록 수정예정
@RequiredArgsConstructor
public class BoardController {
  private static final String BRONZE_PAGE = "member/BRONZE/board"; //보드 홈
  private static final String BRONZE_PAGE_POST = "member/BRONZE/board/post";

  private final BoardService boardService;

  @GetMapping("/post")
  public String writePostForm(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
    log.info("현재 로그인한 사용자: {}", userDetails.getUsername());
    model.addAttribute("board", new BoardRequestDto(null, null));
    return BRONZE_PAGE_POST;
  }

  @PostMapping("/post")
  public String writePost(@ModelAttribute("board") BoardRequestDto boardRequestDto,
                          @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
    log.info("제목: {}, 내용: {}", boardRequestDto.getTitle(), boardRequestDto.getBody());
    log.info("작성자: {}", userDetails.getUsername());
    boardService.save(boardRequestDto, userDetails.getUsername());  // User 엔티티 전체를 전달
    return BRONZE_PAGE;  // POST-Redirect-GET 패턴을 위해 redirect 사용
  }

}
