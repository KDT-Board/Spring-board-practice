package board.springboardpractice.dto;

import board.springboardpractice.domain.Comment;
import lombok.Data;

@Data
public class CommentCreateRequest {

  private String body;

  public Comment toEntity(Board board, User user) {
    return Comment.builder()
            .user(user)
            .board(board)
            .body(body)
            .build();
  }
}
