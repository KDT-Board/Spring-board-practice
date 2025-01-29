package board.springboardpractice.config.security;

import board.springboardpractice.util.jwt.filter.JwtAuthenticationFilter;
import board.springboardpractice.util.jwt.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtTokenProvider jwtTokenProvider;

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    //white list (Spring Security 체크 제외 목록)

    // http request 인증 설정
    http.authorizeHttpRequests(authorize -> authorize
            //해당 api에 대해서는 모든 요청을 허가
            .requestMatchers("/users/sign-in", "/users/sign-up").permitAll()
            //USER 권한이 있어야 요청 가능
            .requestMatchers("/users/test").hasRole("USER")
            // 그 외 요청에서는 인증을 필요로함
            .anyRequest().authenticated()
    );

    // form login disable
    http.formLogin(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 미사용
        );

    // before filter
    http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
