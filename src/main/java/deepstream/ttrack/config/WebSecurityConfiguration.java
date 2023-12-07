package deepstream.ttrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfiguration {

    private final AuthenticationEntryPointJwt authenticationEntryPointJwt;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;



    public WebSecurityConfiguration(
                                    AuthenticationEntryPointJwt authenticationEntryPointJwt,
                                    CustomAccessDeniedHandler customAccessDeniedHandler
                                    ) {
        this.authenticationEntryPointJwt = authenticationEntryPointJwt;
        this.customAccessDeniedHandler = customAccessDeniedHandler;

    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationJwtFilter authenticationJwtTokenFilter() {
        return new AuthenticationJwtFilter();
    }

    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/user/register",
            "/user/login",
            "/api/call-information/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable().cors()
                .and().formLogin().disable()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointJwt)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/failed/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterAfter(authenticationJwtTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
