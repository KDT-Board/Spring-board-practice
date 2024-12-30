package board.springboardpractice.dto.req;

import lombok.Getter;

@Getter
public class BoardRequestDto {
  private String title;
  private String body;

  public BoardRequestDto(String title, String body) {
    this.title = title;
    this.body = body;
  }
}
