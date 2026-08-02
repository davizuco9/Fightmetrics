package com.david.fightmetrics.config;

import com.david.fightmetrics.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(
            CustomUserDetailsService customUserDetailsService
    ) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(customUserDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            DaoAuthenticationProvider authenticationProvider
    ) throws Exception {

        http
                .authenticationProvider(authenticationProvider)

                .authorizeHttpRequests(auth -> auth

                        // Recursos y páginas públicas
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/access-denied",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // Formularios administrativos de luchadores
                        .requestMatchers(
                                HttpMethod.GET,
                                "/fighters/new",
                                "/fighters/*/edit"
                        ).hasRole("ADMIN")

                        // Formularios administrativos de eventos
                        .requestMatchers(
                                HttpMethod.GET,
                                "/events/new",
                                "/events/*/edit"
                        ).hasRole("ADMIN")

                        // Formularios administrativos de rankings
                        .requestMatchers(
                                HttpMethod.GET,
                                "/rankings/new",
                                "/rankings/*/edit"
                        ).hasRole("ADMIN")

                        // Todas las rutas de gestión de combates
                        .requestMatchers(
                                "/fights/**"
                        ).hasRole("ADMIN")

                        // Crear, modificar o eliminar luchadores
                        .requestMatchers(
                                HttpMethod.POST,
                                "/fighters",
                                "/fighters/**"
                        ).hasRole("ADMIN")

                        // Crear, modificar o eliminar eventos
                        .requestMatchers(
                                HttpMethod.POST,
                                "/events",
                                "/events/**"
                        ).hasRole("ADMIN")

                        // Crear, modificar o eliminar rankings
                        .requestMatchers(
                                HttpMethod.POST,
                                "/rankings",
                                "/rankings/**"
                        ).hasRole("ADMIN")

                        // Procesar el formulario público del comparador
                        .requestMatchers(
                                HttpMethod.POST,
                                "/compare"
                        ).permitAll()

                        // Consulta pública
                        .requestMatchers(
                                HttpMethod.GET,
                                "/fighters",
                                "/fighters/**",
                                "/events",
                                "/events/**",
                                "/rankings",
                                "/rankings/**",
                                "/compare",
                                "/compare/**"
                        ).permitAll()

                        // Favoritos requieren iniciar sesión
                        .requestMatchers(
                                "/favorites/**"
                        ).authenticated()

                        // El resto requiere iniciar sesión
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }
}