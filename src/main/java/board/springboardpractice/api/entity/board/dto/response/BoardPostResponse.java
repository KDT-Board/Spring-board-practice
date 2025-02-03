package board.springboardpractice.api.entity.board.dto.response;

import board.springboardpractice.api.entity.board.domain.Board;
import board.springboardpractice.api.entity.global.Level;
import board.springboardpractice.api.entity.user.domain.User;
import lombok.Builder;

public record BoardPostResponse(
        Level boardLevel,
        Long boardStatus,
        String title,
        String content,
        User user
) {

  @Builder
  public BoardPostResponse(Level boardLevel, Long boardStatus, String title, String content, User user) {
    this.boardLevel = boardLevel;
    this.boardStatus = boardStatus;
    this.title = title;
    this.content = content;
    this.user = user;
  }

  public static BoardPostResponse toResponse(Board board) {
    return BoardPostResponse.builder()
            .boardLevel(board.getBoardLevel())
            .boardStatus(board.getBoardStatus())
            .title(board.getTitle())
            .content(board.getContent())
            .user(board.getUser())
            .build();
  }
}