package board.springboardpractice.config;

import board.springboardpractice.domain.user.UserRole;
import board.springboardpractice.jwt.filter.JWTFilter;
import board.springboardpractice.jwt.util.JWTUtil;
import board.springboardpractice.jwt.filter.LoginFilter;
import board.springboardpractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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

  //AuthenticationManger에 들어갈 인자 주입
  private final AuthenticationConfiguration authenticationConfiguration;
  private final JWTUtil jwtUtil;
  private final UserRepository userRepository;

  //AuthenticationManager Bean 등록
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

    return configuration.getAuthenticationManager();
  }


  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {

    return new BCryptPasswordEncoder();
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
                    .requestMatchers("/login", "/", "/join", "/css/**", "/js/**", "/images/**").permitAll()  // 정적 리소스도 허용                    .requestMatchers("/BRONZE/board").hasAnyAuthority(UserRole.BRONZE.getValue(), UserRole.SILVER.getValue(), UserRole.GOLD.getValue(), UserRole.ADMIN.getValue())
                    .requestMatchers("/member/BRONZE/board/**","/BRONZE/board/**").hasAnyAuthority(UserRole.BRONZE.getValue(), UserRole.SILVER.getValue(),UserRole.GOLD.getValue(), UserRole.ADMIN.getValue())
                    .requestMatchers("/SILVER/board").hasAnyAuthority(UserRole.SILVER.getValue(), UserRole.GOLD.getValue(), UserRole.ADMIN.getValue())
                    .requestMatchers("/GOLD/board").hasAnyAuthority(UserRole.GOLD.getValue(), UserRole.ADMIN.getValue())
                    .requestMatchers("/ADMIN/board").hasAuthority(UserRole.ADMIN.getValue())
                    .anyRequest().authenticated());
    //필터 추가
    http
            .addFilterBefore(new JWTFilter(jwtUtil,userRepository), LoginFilter.class); //LoginFilter 앞에 넣는다는 뜻
    //필터 추가
    http
            .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);

    //세션 설정
    http
            .sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}
