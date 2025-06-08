package am.hhovhann.healthcare_management.security;

import am.hhovhann.healthcare_management.filter.JwtRequestFilter;
import am.hhovhann.healthcare_management.service.UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration // Marks this class as a Spring configuration class
@EnableWebSecurity // Enables Spring Security's web security support
@EnableMethodSecurity // Enables method-level security (e.g., @PreAuthorize)
public class SecurityConfiguration {
    private final JwtRequestFilter jwtRequestFilter; // Injects our JWT filter

    public SecurityConfiguration(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // Defines the password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Uses BCrypt algorithm for password hashing
    }

    // Configures the AuthenticationManager to use our UserService and password encoder
    @Bean
    public AuthenticationManager authenticationManager(
            UserService userService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider =
                new DaoAuthenticationProvider(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    // Configures the SecurityFilterChain for HTTP requests
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless REST APIs
                .cors(
                        cors ->
                                cors.configurationSource(
                                        corsConfigurationSource())) // Enable CORS with custom
                                                                    // configuration
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        // Allow unauthenticated access to registration and login
                                        // endpoints
                                        .requestMatchers("/api/v1/auth/**")
                                        .permitAll()
                                        // Require authentication for all other requests
                                        .anyRequest()
                                        .authenticated())
                .sessionManagement(
                        session ->
                                session
                                        // Set session creation policy to STATELESS as we are using
                                        // JWTs
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add our custom JWT filter before Spring Security's default
        // UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Configures CORS (Cross-Origin Resource Sharing)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow requests from all origins (for development). In production, specify allowed
        // origins.
        configuration.setAllowedOrigins(List.of("*"));
        // Allow specific HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Allow specific headers in requests
        configuration.setAllowedHeaders(
                List.of("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        // Expose specific headers in responses
        configuration.setExposedHeaders(List.of("Authorization", "Content-Type"));
        // Allow credentials (cookies, HTTP authentication)
        configuration.setAllowCredentials(
                false); // Set to true if you need to send cookies/credentials

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this CORS configuration to all paths
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
