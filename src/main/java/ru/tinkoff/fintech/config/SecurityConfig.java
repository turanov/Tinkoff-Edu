package ru.tinkoff.fintech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import ru.tinkoff.fintech.model.Role;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${admin.username}")
    private String adminLogin;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${user.username}")
    private String userLogin;
    @Value("${user.password}")
    private String userPassword;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET, "/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                .mvcMatchers(HttpMethod.POST, "/**").hasRole(Role.ADMIN.name())
                .mvcMatchers(HttpMethod.PUT, "/**").hasRole(Role.ADMIN.name())
                .mvcMatchers(HttpMethod.DELETE, "/**").hasRole(Role.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .csrf().disable()
                .httpBasic();
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username(adminLogin)
                        .password(passwordEncoder().encode(adminPassword))
                        .roles(Role.ADMIN.name())
                        .build(),
                User.builder()
                        .username(userLogin)
                        .password(passwordEncoder().encode(userPassword))
                        .roles(Role.USER.name())
                        .build()
        );
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
