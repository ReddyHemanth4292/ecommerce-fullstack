package com.example.ecommercefinal.config;

import com.example.ecommercefinal.security.CustomUserDetailsService;
import com.example.ecommercefinal.service.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.net.http.HttpRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(auth ->
                                auth.requestMatchers("/api/auth/**")
                                        .permitAll()
                                        .requestMatchers("/public/**").permitAll()
                                        .requestMatchers("/api/**").permitAll()
                                        .requestMatchers("/v3/api-docs/**").permitAll()
                                        .requestMatchers("/swagger-ui/**").permitAll()
                                        .requestMatchers("/actuator/health").permitAll()
                                        .requestMatchers("/actuator/info").hasRole("ADMIN")
                                        .requestMatchers("/actuator/metrics/**").hasRole("ADMIN")
                                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.sendError(
                                            HttpServletResponse.SC_UNAUTHORIZED,
                                            "Unauthorized");
                                }
                        )
                )
        ;
        return http.build();
    }
}
