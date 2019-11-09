package com.llwantedll.webhearts.models.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@ComponentScan("com.llwantedll.webhearts")
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] CORS_ALLOWED_METHODS =
            {"GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"};
    private static final String[] CORS_ALLOWED_HEADERS =
            {"X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization"};
    private static final String CORS_ALLOWED_ORIGIN = "*";

    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomAuthenticationFailHandler failHandler;

    @Autowired
    public SpringSecurityConfig(CustomAuthenticationSuccessHandler successHandler,
                                CustomAuthenticationFailHandler failHandler) {
        this.successHandler = successHandler;
        this.failHandler = failHandler;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/register").not().authenticated()
                .antMatchers("/user", "/create_room", "/game/*").hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                .and()
                .authorizeRequests().antMatchers("/messages").permitAll()
                .and()
                .formLogin().usernameParameter("login").passwordParameter("password")
                .loginPage("/login").successHandler(successHandler).failureHandler(failHandler)
                .and()
                .httpBasic();
    }


    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin(CORS_ALLOWED_ORIGIN);
        corsConfiguration.setAllowedMethods(Arrays.asList(CORS_ALLOWED_METHODS));
        corsConfiguration.setAllowedHeaders(Arrays.asList(CORS_ALLOWED_HEADERS));
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", corsConfiguration.applyPermitDefaultValues());
        return source;
    }
}
