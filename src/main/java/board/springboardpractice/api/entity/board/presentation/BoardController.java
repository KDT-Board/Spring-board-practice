package board.springboardpractice.api.entity.board.presentation;

import board.springboardpractice.api.common.response.entity.ApiResponseEntity;
import board.springboardpractice.api.entity.board.application.BoardService;
import board.springboardpractice.api.entity.board.domain.Board;
import board.springboardpractice.api.entity.board.dto.request.BoardPostRequest;
import board.springboardpractice.api.entity.board.dto.response.BoardPostResponse;
import board.springboardpractice.api.entity.global.Level;
import board.springboardpractice.util.jwt.userdetails.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
  private final BoardService boardService;
  private final CustomUserDetailsService customUserDetailsService;


  @GetMapping("/bronze")
  public ResponseEntity<ApiResponseEntity> bronzeBoards(@AuthenticationPrincipal UserDetails userDetails){
    customUserDetailsService.validateAvailableUser(userDetails,Level.BRONZE);
    List<Board> boardList = boardService.findAll(Level.BRONZE);

    return ApiResponseEntity.successResponseEntity(
            boardList
    );
  }

  @PostMapping("/bronze/write")
  public ResponseEntity<ApiResponseEntity> bronzeBoardWrite(
          @AuthenticationPrincipal UserDetails userDetails,
          @RequestBody BoardPostRequest boardPostRequest
          ){
    customUserDetailsService.validateAvailableUser(userDetails,Level.BRONZE);
    BoardPostResponse boardPostResponse = boardService.save(Board.toEntity(boardPostRequest));

    return ApiResponseEntity.successResponseEntity(
            boardPostResponse
    );
  }
}
