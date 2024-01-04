package com.example.Fiszki.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthFilter;
//    private final AuthenticationProvider authenticationProvider;
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer()
//    {
//            return new WebMvcConfigurer()
//            {
//                public void addCorsMappings(final CorsRegistry registry)
//                {
//                    registry
//                            .addMapping("/**")
//                            .allowedOrigins("http://localhost:4200")
//                            .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS")
//                            .allowedHeaders("*")
//                            .allowCredentials(true)
//                            .maxAge(3600);
//                }
//            };
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
//
//        //                        .loginPage("/login")
//        httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers(new AntPathRequestMatcher("/flashcards/auth/**")).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .sessionManagement((sessionManagement) -> sessionManagement
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
////                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll);
//
//
//        return httpSecurity.build();
//    }
//}

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(final CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //Make the below setting as * to allow connection from any hos
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource())) // Dodaj tę linię dla obsługi CORS
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(new AntPathRequestMatcher("/flashcards/auth/**")).permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}