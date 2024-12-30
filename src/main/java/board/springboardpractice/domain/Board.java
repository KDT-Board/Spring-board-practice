package board.springboardpractice.domain;

import board.springboardpractice.domain.global.BaseEntity;
import board.springboardpractice.dto.req.BoardRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title; //제목
  private String body; //본문

  @ManyToOne(fetch = FetchType.LAZY)
  private User user; //작성자

  @OneToMany(mappedBy = "board", orphanRemoval = true)
  private List<Like> likes; //좋아요
  private Integer likeCnt;

  @OneToMany(mappedBy = "board", orphanRemoval = true)
  private List<Comment> comments; //댓글
  private Integer commentCnt;     //댓글 수

  public void update(BoardRequestDto dto) {
    this.title = dto.getTitle();
    this.body = dto.getBody();
  }

  public void likeChange(Integer likeCnt) {
    this.likeCnt = likeCnt;
  }

  public void commentChange(Integer commentCnt) {
    this.commentCnt = commentCnt;
  }



}
