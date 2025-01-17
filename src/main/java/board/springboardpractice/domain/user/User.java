package board.springboardpractice.domain.user;

import board.springboardpractice.domain.Board;
import board.springboardpractice.domain.Like;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String loginId; //아이디
  private String password; //비밀번호
  private String nickname; //닉네임
  private LocalDateTime createdAt; //가입시간
  private Integer receivedLikeCnt; //유저가 받은 좋아요 수

  @Enumerated(EnumType.STRING)
  private UserRole userRole; //보드 접근 권한

  private Role role; //Spring security 권한

  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<Board> boards; //작성글

  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<Like> likes;  //유저가 누른 좋아요

  @OneToMany(mappedBy = "user", orphanRemoval = true)
  private List<Board> comments; //댓글

  public void likeChange(Integer receivedLikeCnt){
    this.receivedLikeCnt = receivedLikeCnt;
    if (this.receivedLikeCnt >= 10 && this.userRole.equals(UserRole.SILVER)){
      this.userRole = UserRole.GOLD;
    }
  }

  public void edit(String newPassword, String newNickname){
    this.password = newPassword;
    this.nickname = newNickname;
  }

  public void changeRole(){
    if (userRole.equals(UserRole.BRONZE)) userRole = UserRole.SILVER;
    else if (userRole.equals(UserRole.SILVER)) userRole = UserRole.GOLD;
    else if (userRole.equals(UserRole.GOLD)) userRole = UserRole.BLACKLIST;
    else if (userRole.equals(UserRole.BLACKLIST)) userRole = UserRole.BRONZE;

  }

  public static User of(String loginId, String password, String nickname) {
    return User.builder()
            .loginId(loginId)
            .password(password)
            .nickname(nickname)
            .createdAt(LocalDateTime.now())
            .receivedLikeCnt(0)
            .userRole(UserRole.BRONZE)  //기본 권한을 BRONZE로 설정
            .build();
  }



}
