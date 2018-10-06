package com.application.restaurantbooking.config;

import com.application.restaurantbooking.jwt.JwtAuthenticationEntryPoint;
import com.application.restaurantbooking.jwt.jwtService.JwtClientService;
import com.application.restaurantbooking.jwt.jwtService.JwtRestorerService;
import com.application.restaurantbooking.jwt.jwtToken.JwtAuthorizationTokenFilter;
import com.application.restaurantbooking.jwt.jwtToken.JwtTokenUtil;
import com.application.restaurantbooking.persistence.model.AuthorityName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtAuthenticationEntryPoint unauthorizedHandler;

    private JwtTokenUtil jwtTokenUtil;

    private JwtRestorerService jwtRestorerService;

    private JwtClientService jwtClientService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.route.authentication.path}")
    private String authenticationPath;

    @Autowired
    public WebSecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                             JwtTokenUtil jwtTokenUtil,
                             JwtRestorerService jwtRestorerService,
                             JwtClientService jwtClientService,
                             BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.unauthorizedHandler = jwtAuthenticationEntryPoint;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtRestorerService = jwtRestorerService;
        this.jwtClientService = jwtClientService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> authenticationProviders = new ArrayList<>();
        authenticationProviders.add(new UserAuthenticationProvider(AuthorityName.ROLE_RESTORER, jwtRestorerService, bCryptPasswordEncoder));
        authenticationProviders.add(new UserAuthenticationProvider(AuthorityName.ROLE_CLIENT, jwtClientService, bCryptPasswordEncoder));
        return new ProviderManager(authenticationProviders);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // AuthenticationTokenFilter will ignore the below paths
        web
                .ignoring()
                .antMatchers(
                        HttpMethod.POST
                )
                // allow anonymous resource requests
                .and()
                .ignoring()
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/register",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                );
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();

        // Custom JWT based security filter
        JwtAuthorizationTokenFilter authenticationTokenFilter = new JwtAuthorizationTokenFilter(jwtRestorerService, jwtClientService, jwtTokenUtil, tokenHeader);
        httpSecurity
                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity
                .headers()
                .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();
    }
}
