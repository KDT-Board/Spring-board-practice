package board.springboardpractice.config;

import board.springboardpractice.util.jwt.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
    //white list (Spring Security 체크 제외 목록)

    // http request 인증 설정
    http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers(permitAllWhiteList).permitAll()
            // 사용자 삭제는 관리자 권한만 가능
            .requestMatchers(HttpMethod.DELETE, "/user").hasRole(RoleName.ROLE_ADMIN.getRole())
            // 그 외 요청 체크
            .anyRequest().authenticated()
    );

    // form login disable
    http.formLogin(AbstractHttpConfigurer::disable);

    // logout disable
    http.logout(AbstractHttpConfigurer::disable);

    // csrf disable
    http.csrf(AbstractHttpConfigurer::disable);

    // session management
    http.sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 미사용
    );

    // before filter
    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    //build
    return httpSecurity.build();
  }
}
