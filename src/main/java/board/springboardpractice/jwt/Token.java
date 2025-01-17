package board.springboardpractice.jwt;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Token")
public class Token implements Serializable {
  @Id
  private String identifier;
  private String token;

  public Token(String identifier, String token) {
    this.identifier = identifier;
    this.token = token;
  }

  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
