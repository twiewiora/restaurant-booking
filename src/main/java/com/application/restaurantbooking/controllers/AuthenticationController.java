package com.application.restaurantbooking.controllers;

import com.application.restaurantbooking.exceptions.authentication.AuthenticationException;
import com.application.restaurantbooking.jwt.RegistrationValidation;
import com.application.restaurantbooking.jwt.jwtModel.JwtAuthenticationRequest;
import com.application.restaurantbooking.jwt.jwtService.JwtAuthenticationResponse;
import com.application.restaurantbooking.jwt.jwtService.JwtUserDetailsService;
import com.application.restaurantbooking.jwt.jwtToken.JwtTokenUtil;
import com.application.restaurantbooking.persistence.builder.RestorerBuilder;
import com.application.restaurantbooking.persistence.model.Authority;
import com.application.restaurantbooking.persistence.model.Restorer;
import com.application.restaurantbooking.persistence.repository.AuthorityRepository;
import com.application.restaurantbooking.persistence.service.RestorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.Objects;

import static com.application.restaurantbooking.persistence.model.AuthorityName.ROLE_ADMIN;
import static com.application.restaurantbooking.persistence.model.AuthorityName.ROLE_RESTORER;

@RestController
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private RestorerService restorerService;

    private JwtUserDetailsService jwtUserDetailsService;

    private AuthorityRepository authorityRepository;

    private JwtTokenUtil jwtTokenUtil;

    private UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    BCryptPasswordEncoder bCryptPasswordEncoder,
                                    RestorerService restorerService,
                                    JwtUserDetailsService jwtUserDetailsService,
                                    AuthorityRepository authorityRepository,
                                    JwtTokenUtil jwtTokenUtil,
                                    @Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.restorerService = restorerService;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.authorityRepository = authorityRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // Reload password post-security so we can generate the token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @RequestMapping(value = "${jwt.route.authentication.register}", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Object> register(@RequestBody JwtAuthenticationRequest registrationRequest) {
        try {
            jwtUserDetailsService.loadUserByUsername(registrationRequest.getUsername());
            return ResponseEntity.noContent().build();
        } catch (UsernameNotFoundException e) {
            try {
                RegistrationValidation registrationValidation = new RegistrationValidation();
                if (registrationValidation.validate(registrationRequest)) {
                    LinkedList<Authority> authorityList = new LinkedList<>();
                    authorityList.add(authorityRepository.findByName(ROLE_RESTORER));
                    authorityList.add(authorityRepository.findByName(ROLE_ADMIN));
                    RestorerBuilder restorerBuilder = new RestorerBuilder();
                    Restorer restorer = restorerBuilder.username(registrationRequest.getUsername())
                            .password(bCryptPasswordEncoder.encode(registrationRequest.getPassword()))
                            .authorities(authorityList)
                            .build();
                    restorerService.createRestorer(restorer);
                    return ResponseEntity.noContent().build(); //correct
                }
            } catch (Exception ex) {
                return ResponseEntity.noContent().build(); //incorrect data
            }

        }
        return ResponseEntity.ok(registrationRequest);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /**
     * Authenticates the user. If something is wrong, an {@link AuthenticationException} will be thrown
     */
    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }


    class Message {
        private String text;

        Message(String text) {
            this.text = text;
        }

        public Message() {
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
