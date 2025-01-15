package board.springboardpractice.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TokenStatus {
  AUTHENTICATED,
  EXPIRED,
  INVALID
}
