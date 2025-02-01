package board.springboardpractice.api.entity.global;

public enum Level {
  BRONZE("브론즈"),
  SILVER("실버"),
  GOLD("골드");

  private String kor;

  Level(String kor) {
    this.kor = kor;
  }
}
