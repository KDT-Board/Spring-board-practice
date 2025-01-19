package board.springboardpractice.api.user.domain.entity;

import board.springboardpractice.api.common.entity.RegModDt;
import board.springboardpractice.api.user.domain.entity.value.LoginInfo;
import board.springboardpractice.api.user.domain.entity.value.RoleInfo;
import board.springboardpractice.api.user.domain.entity.value.UserInfo;
import board.springboardpractice.api.user.dto.request.UserAddRequestDTO;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends RegModDt {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Embedded
  private LoginInfo loginInfo;

  @Embedded
  private RoleInfo roleInfo;

  @Embedded
  private UserInfo userInfo;

  /**
   * UserAddRequestDTO to User Entity
   *
   * @param dto UserAddRequestDTO
   * @return User Entity
   */
  public static User of(UserAddRequestDTO dto) {
    // Login Info
    LoginInfo inputLoginInfo = LoginInfo.builder()
            .userId(dto.getUserId())
            .password(dto.getPassword())
            .build();

    // User Info
    UserInfo inputUserInfo = UserInfo.builder()
            .name(dto.getName())
            .tel(dto.getTel())
            .build();

    // Role Info
    RoleInfo inputRoleInfo = RoleInfo.builder()
            .roleName(dto.getRoleName())
            .build();

    return User.builder()
            .loginInfo(inputLoginInfo)
            .userInfo(inputUserInfo)
            .roleInfo(inputRoleInfo)
            .build();
  }

}