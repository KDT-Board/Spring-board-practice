package board.springboardpractice.api.entity.user.domain;

import board.springboardpractice.api.common.entity.RegModDt;
import board.springboardpractice.api.entity.board.domain.Board;
import board.springboardpractice.api.entity.global.Level;
import board.springboardpractice.api.entity.user.dto.request.SignUpRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends RegModDt implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", updatable = false, unique = true, nullable = false)
  private Long id;

  @NotNull
  @Column()
  private String username;

  @NotNull
  @Column()
  private String password;

  private String nickname;

  @NotNull
  private String email;

  private String profileImg;

  @Enumerated(EnumType.STRING)
  private LoginType loginType;

  @ElementCollection(fetch = FetchType.EAGER)
  @Builder.Default
  private List<String> roles = new ArrayList<>();

  // ------
  @OneToMany(mappedBy = "user")
  private List<Board> boards = new ArrayList<>();

  //level 업그레이드
  public void upgradeUserLevel() {
    Level currentLevel = Level.valueOf(roles.get(1));

    long currentLevelBoardCount = boards.stream()
            .filter(board -> board.getBoardLevel() == currentLevel)
            .count();

    if (currentLevelBoardCount >= 10 && currentLevel == Level.BRONZE) {
      roles.set(1, Level.SILVER.name());
    } else if (currentLevelBoardCount >= 20 && currentLevel == Level.SILVER) {
      roles.set(1, Level.GOLD.name());
    }
  }

  public User(String username, String password, Collection<? extends GrantedAuthority> authorities){
    this.username = username;
    this.password = password;
    this.roles = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
  }

  //dto -> entity
  public static User toEntity(SignUpRequest request, PasswordEncoder passwordEncoder){
    return User.builder()
            .username(request.username())
            .password(passwordEncoder.encode(request.password())) //단방향 암호화
            .nickname(request.nickname())
            .email(request.email())
            .profileImg(request.profileImg())
            .loginType(LoginType.ORIGINAL)
            .roles(List.of("ROLE_USER", Level.BRONZE.name()))
            .build();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
