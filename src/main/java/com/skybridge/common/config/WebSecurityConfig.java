package com.skybridge.common.config;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    //private final JwtUtil jwtUtil;

    //@Value("${web.ignoring.url}")
    //private String[] ignoringUrl;

    //@Value("${web.authorize.url}")
    //private String[] authorizeUrl;

    //private final LogService logService;

    @Bean
    public SecurityFilterChain securityFilterChain(final @NotNull  HttpSecurity http) throws Exception {
        http.httpBasic(HttpBasicConfigurer::disable)
                .cors(withDefaults())
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
                                //.requestMatchers("/**").permitAll().anyRequest().authenticated()
                                //.requestMatchers(authorizeUrl).permitAll().anyRequest().authenticated()
                                .requestMatchers("/**").permitAll()

                )
                //.addFilterBefore(new JwtAuthFilter(jwtUtil, authorizeUrl, logService), UsernamePasswordAuthenticationFilter.class)
                //.exceptionHandling((exception)-> exception.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
        ;
        return http.build();
    }
}
