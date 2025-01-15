package board.springboardpractice.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BusinessExceptionStatus {

  MEMBER_NOT_FOUND("회원을 찾을 수 없습니다.", 404),
  INVALID_JWT("유효하지 않은 토큰입니다.", 401),
  INVALID_EXPIRED_JWT("만료된 토큰입니다.", 401);

  private final String message;
  private final int statusCode;


}
