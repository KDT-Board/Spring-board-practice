package board.springboardpractice.domain.user;

import lombok.Getter;

@Getter
public enum UserRole {
  BRONZE("BRONZE"),
  SILVER("SILVER"),
  GOLD("GOLD"),
  BLACKLIST("BLACKLIST"),
  ADMIN("ADMIN");

  private final String value;

  UserRole(String value) {
    this.value = value;
  }
}
