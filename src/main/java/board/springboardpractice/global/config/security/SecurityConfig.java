package board.springboardpractice.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@Bean
//public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//  http
//          .csrf((csrfConfig)->
//                  csrfConfig.disable()
//          )
//          .headers((headerConfig)->
//                  headerConfig.frameOptions((frameOptionsConfig ->
//                          frameOptionsConfig.sameOrigin())
//                  )
//          )
//          .formLogin((formLoginConfig)->
//                  formLoginConfig.disable()
//          )
//          .authorizeHttpRequests((auth) -> auth
//                  .requestMatchers(HttpMethod.GET, "/**").permitAll()
//                  .anyRequest().authenticated()
//          );
//  return http.build();
//}