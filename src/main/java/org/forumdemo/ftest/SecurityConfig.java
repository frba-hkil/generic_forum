package org.forumdemo.ftest;

import org.forumdemo.ftest.service.ForumUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/api/v1/posts/write").hasRole("USER")
                                .requestMatchers("/api/v1/posts/*/new_comment").hasRole("USER")
                                .requestMatchers("/api/v1/posts/*/upvote").hasRole("USER")
                                .requestMatchers("/api/v1/posts/*/down").hasRole("USER")
                                .requestMatchers("/**").permitAll()
                )
                .formLogin(x -> x.loginPage("/api/v1/users/login").defaultSuccessUrl("/api/v1/posts"))
                .logout((logout) -> logout.invalidateHttpSession(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/users/logout"))
                        .logoutSuccessUrl("/api/v1/posts"));

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
//    @Bean
//    public AuthenticationManager authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
//
//        return new ProviderManager(daoAuthenticationProvider);
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
