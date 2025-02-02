package board.springboardpractice.api.entity.board.dto.request;

import board.springboardpractice.api.entity.global.Level;
import board.springboardpractice.api.entity.user.domain.User;
import lombok.Builder;

public record BoardPostRequest(
        Level boardLevel,
        Long boardStatus,
        String title,
        String content,
        User user
) {
  @Builder
  public BoardPostRequest(Level boardLevel, Long boardStatus, String title, String content, User user) {
    this.boardLevel = boardLevel;
    this.boardStatus = boardStatus;
    this.title = title;
    this.content = content;
    this.user = user;
  }
}
