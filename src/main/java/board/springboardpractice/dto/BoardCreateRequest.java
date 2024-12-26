package board.springboardpractice.dto;

import board.springboardpractice.domain.Board;
import board.springboardpractice.domain.BoardCategory;
import board.springboardpractice.domain.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BoardCreateRequest {

  private String title;
  private String body;
  private MultipartFile uploadImage;

  public Board toEntity(BoardCategory category, User user) {
    return Board.builder()
            .user(user)
            .category(category)
            .title(title)
            .body(body)
            .likeCnt(0)
            .commentCnt(0)
            .build();
  }
}
