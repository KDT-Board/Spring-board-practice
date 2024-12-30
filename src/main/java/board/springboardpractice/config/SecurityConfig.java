package board.springboardpractice.config;

import board.springboardpractice.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  //AuthenticationManger에 들어갈 인자 주입
  private final AuthenticationConfiguration authenticationConfiguration;

  //AuthenticationManager Bean 등록
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

    return configuration.getAuthenticationManager();
  }


  @Bean
  public PasswordEncoder passwordEncoder() {

    return PasswordEncoderFactories.createDelegatingPasswordEncoder();

  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    //csrf disable
    http
            .csrf((auth) -> auth.disable());

    //Form 로그인 방식 disable
    http
            .formLogin((auth) -> auth.disable());

    //http basic 인증 방식 disable
    http
            .httpBasic((auth) -> auth.disable());

    //경로별 인가 작업
    http
            .authorizeHttpRequests((auth) -> auth
                    .requestMatchers("/login", "/", "/join").permitAll()
                    .requestMatchers("/admin").hasRole("ADMIN")
                    .anyRequest().authenticated());
    //필터 추가
    http
            .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);

    //세션 설정
    http
            .sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}
