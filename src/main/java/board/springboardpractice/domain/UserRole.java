package board.springboardpractice.domain;

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
