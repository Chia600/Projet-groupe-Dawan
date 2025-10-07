package com.dawanproject.booktracker.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     *
     * @param http The HttpSecurity configuration object.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Désactiver CSRF pour les APIs REST
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/users/register").permitAll() // Endpoint public pour l'inscription
                        .anyRequest().authenticated() // Tous les autres endpoints nécessitent une authentification
                )
                .httpBasic(httpBasic -> {}) // Authentification de base
                .formLogin(form -> {}); // Connexion par formulaire

        return http.build();
    }

    /**
     * Provides a PasswordEncoder bean for hashing passwords.
     *
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures an in-memory user details service for testing purposes.
     *
     * @return An InMemoryUserDetailsManager with a default user.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        var user = User.withUsername("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}