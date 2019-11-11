package com.llwantedll.webhearts.models.configs.security;

import com.llwantedll.webhearts.models.configs.ConfigurationData;
import com.llwantedll.webhearts.models.configs.PathConfiguration;
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

    private static final String USERNAME_PARAMETER = "login";
    private static final String PASSWORD_PARAMETER = "password";

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
                .antMatchers(
                        PathConfiguration.LOGIN_PAGE,
                        PathConfiguration.REGISTRATION_PAGE)
                .not().authenticated()
                .antMatchers(
                        PathConfiguration.USER_CONTENT_PATH,
                        PathConfiguration.CREATE_ROOM_PAGE,
                        PathConfiguration.GAME_PATTERN_PATH)
                .hasAnyAuthority(UserRole.ADMIN.name(), UserRole.USER.name())
                .and()
                .authorizeRequests()
                .antMatchers(PathConfiguration.STOMP_ENDPOINT)
                .permitAll()
                .and()
                .formLogin().usernameParameter(USERNAME_PARAMETER).passwordParameter(PASSWORD_PARAMETER)
                .loginPage(PathConfiguration.LOGIN_PAGE).successHandler(successHandler).failureHandler(failHandler)
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

        corsConfiguration.addAllowedOrigin(ConfigurationData.CORS_ALLOWED_ORIGIN);
        corsConfiguration.setAllowedMethods(Arrays.asList(ConfigurationData.CORS_ALLOWED_METHODS));
        corsConfiguration.setAllowedHeaders(Arrays.asList(ConfigurationData.CORS_ALLOWED_HEADERS));
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration(ConfigurationData.CORS_CONFIGURATION_PATH, corsConfiguration.applyPermitDefaultValues());
        return source;
    }
}
