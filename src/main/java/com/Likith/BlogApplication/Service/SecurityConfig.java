package com.Likith.BlogApplication.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain( HttpSecurity http) throws Exception {

        http

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",

                                "/home",
                                "/home/",

                                "/home/blog/**"
                        )
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/home/blog/*/comment"
                        )
                        .permitAll()

                        .requestMatchers(
                                "/home/createBlog",
                                "/home/updateBlog/**",
                                "/home/deleteBlog/**"
                        )
                        .hasAnyRole("AUTHOR", "ADMIN")

                        .requestMatchers(
                                "/admin/**",
                                "/tag/**"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                "/comments/edit/**",
                                "/comments/update/**",
                                "/comments/delete/**"
                        )
                        .authenticated()
                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form

                        .loginPage("/login")

                        .defaultSuccessUrl(
                                "/home",
                                true
                        )

                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                );
        http.exceptionHandling(exception -> exception
                .accessDeniedPage("/access-denied")
        );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider( PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

}