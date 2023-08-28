package com.manglik.jwtdemo.config;

import com.manglik.jwtdemo.Security.JwtAuthenticationEntrypoint;
import com.manglik.jwtdemo.Security.JwtAuthernticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationEntrypoint jwtAuthenticationEntrypoint;
    @Autowired
    private JwtAuthernticationFilter jwtAuthernticationFilter;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                        .authorizeHttpRequests(auth -> auth.requestMatchers("/home/**").authenticated().requestMatchers("/auth/login").permitAll().requestMatchers("/auth/create-user").permitAll().anyRequest().authenticated())
                                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntrypoint))
                                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));



//        http.csrf(
//                csrf -> {
//                    try {
//                        csrf.disable()
//                                .cors(cors -> {
//                                            try {
//                                                cors.disable()
//                                                        .authorizeHttpRequests(
//                                                                req ->
//                                                                        req.requestMatchers("/home/**").authenticated()
//                                                                        .requestMatchers("/auth/create-user")
//                                                                        .permitAll()
//                                                                        .requestMatchers("/auth/login").permitAll()
//                                                                        .anyRequest()
//                                                                        .authenticated())
//                                                        .exceptionHandling(ex-> ex.authenticationEntryPoint(jwtAuthenticationEntrypoint))
//                                                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))   ;
//                                            } catch (Exception e) {
//                                                throw new RuntimeException(e);
//                                            }
//                                        }
//                                );
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//        );
        http.addFilterBefore(jwtAuthernticationFilter ,UsernamePasswordAuthenticationFilter.class );
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

}
