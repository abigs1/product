package com.product.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.product.config.jwt.JwtAuthFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfig corsConfig) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
                auth -> auth

                // Ruta interna de Spring para errores
                .requestMatchers("/error").permitAll()

                // Customer y Admin: categorias activas
                .requestMatchers(HttpMethod.GET, "/category/active").hasAnyAuthority("Administrator", "User")

                // Customer y Admin: detalle de un producto y sus imagenes
                .requestMatchers(HttpMethod.GET, "/product/{id}").hasAnyAuthority("Administrator", "User")
                .requestMatchers(HttpMethod.GET, "/product/{id}/image").hasAnyAuthority("Administrator", "User")

                // Todo lo demas: solo Admin
                .anyRequest().hasAuthority("Administrator")
                )
        .cors(cors -> cors.configurationSource(corsConfig))
        .httpBasic(Customizer.withDefaults())
        .formLogin(form -> form.disable())
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
