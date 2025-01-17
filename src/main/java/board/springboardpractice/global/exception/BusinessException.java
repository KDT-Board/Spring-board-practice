package board.springboardpractice.global.exception;

import lombok.Getter;

import java.io.IOException;

@Getter
public class BusinessException extends IOException{
  private final String message;
  private final int statusCode;

  public BusinessException(BusinessExceptionStatus status) {
    this.message = status.getMessage();
    this.statusCode = status.getStatusCode();
  }
}