package board.springboardpractice.api.entity.user;

import board.springboardpractice.api.common.entity.RegModDt;
import board.springboardpractice.api.entity.user.dto.request.SignUpRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
  @GeneratedValue
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

  @ElementCollection(fetch = FetchType.EAGER)
  @Builder.Default
  private List<String> roles = new ArrayList<>();

  public User(String username, String password, Collection<? extends GrantedAuthority> authorities){
    this.username = username;
    this.password = password;
    this.roles = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
  }

  //dto -> entity
  public static User toEntity(SignUpRequest request){
    return User.builder()
            .username(request.username())
            .password(request.password())
            .nickname(request.nickname())
            .email(request.email())
            .profileImg(request.profileImg())
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
