//package board.springboardpractice.global.jwt;
//
//import board.springboardpractice.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//  private final JwtService jwtService;
//  private final UserRepository userRepository;
//
//  @Bean
//  public SecurityFilterChain filterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
//    // REST API이므로 basic auth 및 csrf 보안을 사용하지 않음
//    http.httpBasic(AbstractHttpConfigurer::disable);
//    http.csrf(AbstractHttpConfigurer::disable);
//
//    // JWT를 사용하기 때문에 세션을 사용하지 않음
//    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//    // CORS 설정 적용
//    http.cors(customizer -> customizer.configurationSource(corsConfigurationSource));
//
//    // 메서드 권한 설정
//    http.authorizeHttpRequests(auth -> auth
//                    .requestMatchers( "/users/join", "/users/login","/main")
//                    .permitAll()
//                    .anyRequest().authenticated())
//            .formLogin(form -> form
//                    .loginPage("/users/login")
//                    .defaultSuccessUrl("/main", true)
//                    .permitAll()
//            )
//            .addFilterBefore(new AuthFilter(jwtService, userRepository), UsernamePasswordAuthenticationFilter.class);
//    return http.build();
//  }
//}
