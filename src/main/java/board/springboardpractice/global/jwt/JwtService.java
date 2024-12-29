//package board.springboardpractice.global.jwt;
//
//import board.springboardpractice.domain.User;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
//@Service
//@Slf4j
//public class JwtService {
//  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; //60분
//  private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; //7일
//  private static final String PREFIX_REFRESH = "REFRESH: ";
//  private static final String PREFIX_LOGOUT_ACCESS = "LOGOUT_ACCESS: ";
//  private static final String PREFIX_LOGOUT_REFRESH = "LOGOUT_REFRESH: ";
//
//  //액세스 토큰 생성
//  protected String createAccessToken(User loginUser) {
//    long now = new Date().getTime();
//
//    return Jwts.builder()
//            .setSubject(loginUser.getId())
//            .claim("grade", loginUser.getUserRole())
//            .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
//            .signWith(key, SignatureAlgorithm.HS256)
//            .compact();
//  }
//}