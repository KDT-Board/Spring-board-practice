package board.springboardpractice.api.entity.user.application;

import board.springboardpractice.util.jwt.token.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class ReissueService {
  private JwtTokenProvider jwtTokenProvider;

//  public String reissueAccessToken(String token) {
//    //token에서 email 정보 뽑아내기
//    Claims claims1 = jwtTokenProvider.getUserInfoFromToken(token);
//    String email = (String) claims1.get("email");
//
//    //redis에서 email key에 해당하는 value (refreshtoken) 가져오기
//    Claims claims2 = jwtTokenProvider.getUserInfoFromToken(token);
//    jwtTokenProvider.generateToken()
//            //새로 발급한 accesstoken 리턴
//    return ;
//  }
}
