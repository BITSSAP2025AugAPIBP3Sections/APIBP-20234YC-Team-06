package com.privychat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder encoder) {
    // Allow override via environment variables
    String username = Optional.ofNullable(System.getenv("PRIVYCHAT_ADMIN_USER")).orElse("admin");
    String rawPassword = Optional.ofNullable(System.getenv("PRIVYCHAT_ADMIN_PASSWORD")).orElse("privychat");
    // If an encoded hash is provided (starts with {bcrypt}) leave as is
    String passwordToUse = rawPassword.startsWith("{bcrypt}") ? rawPassword : encoder.encode(rawPassword);
    UserDetails admin = User.withUsername(username)
        .password(passwordToUse)
        .roles("USER", "ADMIN")
        .build();
    return new InMemoryUserDetailsManager(admin);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // Allow user registration without auth
            .requestMatchers(HttpMethod.POST, "/users").permitAll()
            .requestMatchers(HttpMethod.GET, "/users/*").permitAll()
            // GraphQL now requires authentication
            .requestMatchers("/graphql").authenticated()
            // Everything else requires authentication
            .anyRequest().authenticated()
        )
        .httpBasic(httpBasic -> {})
        .formLogin(form -> form.disable());
    return http.build();
  }
}
