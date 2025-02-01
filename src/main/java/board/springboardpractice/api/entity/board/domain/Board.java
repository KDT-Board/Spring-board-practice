package board.springboardpractice.api.entity.board.domain;

import board.springboardpractice.api.common.entity.RegModDt;
import board.springboardpractice.api.entity.board.dto.request.BoardPostRequest;
import board.springboardpractice.api.entity.global.Level;
import board.springboardpractice.api.entity.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Board extends RegModDt {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", updatable = false, unique = true, nullable = false)
  private Long id;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Level boardLevel;

  @NotNull
  private Long boardStatus;
  /**
   * 삭제 : 10
   * 권한없음 : 20
   * 보임 : 50
   */

  @NotNull
  @Column()
  private String title;

  @NotNull
  @Column(columnDefinition = "TEXT")
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Builder
  private Board(Level boardLevel, Long boardStatus, String title, String content, User user) {
    this.boardLevel = boardLevel;
    this.boardStatus = boardStatus;
    this.title = title;
    this.content = content;
    this.user = user;
  }

  public static Board of(Level boardLevel, Long boardStatus, String title, String content, User user){
    return Board.builder()
            .boardLevel(boardLevel)
            .boardStatus(boardStatus)
            .title(title)
            .content(content)
            .user(user)
            .build();
  }

  public static Board toEntity(BoardPostRequest boardPostRequest) {
    return Board.of(
            boardPostRequest.boardLevel(),
            boardPostRequest.boardStatus(),
            boardPostRequest.title(),
            boardPostRequest.content(),
            boardPostRequest.user()
    );
  }
}
