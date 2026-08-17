package com.mithilesh.eventmanagement.configuration;


import com.mithilesh.eventmanagement.security.JwtFilter;
import com.mithilesh.eventmanagement.security.MyUserDetailService;
import com.sun.net.httpserver.HttpsConfigurator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ConfigSecurity {

    private final MyUserDetailService myUserDetailService;
    private final JwtFilter jwtFilter;

    /**
     * Configure the Http security rules, public endpoint, authorization and jwt authentication.
     *
     * @param http contains http Security configuration
     * @return the configured security http filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){

        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/auth/**",
                                "/event/search",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                "/event",
                                "/event/{id}"
                                ).permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/event/{id}/registrations"
                        ).hasAnyRole("USER")

                        .requestMatchers(HttpMethod.DELETE,
                                "/event/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,"/event").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/event/{id}").hasRole("ADMIN")

                        .requestMatchers(
                                "/favorites/**",
                                "/api/favorites"
                        ).hasRole("USER")

                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * To create Password encoder
     * Used to store it on the DB in encoded form
     *
     * @return the password encoder to encode the password
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    /**
     * To control the log in logic
     *
     * @return the AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(myUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * To manage the authentication
     *
     * @param config contains the Authentication configuration
     * @return the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config){
        return config.getAuthenticationManager();
    }
}
