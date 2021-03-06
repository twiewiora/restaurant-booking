package com.application.restaurantbooking.controllers;

import com.application.restaurantbooking.exceptions.authentication.AuthenticationException;
import com.application.restaurantbooking.jwt.RegistrationValidation;
import com.application.restaurantbooking.jwt.jwtmodel.JwtAuthenticationRequest;
import com.application.restaurantbooking.jwt.jwtservice.JwtAuthenticationResponse;
import com.application.restaurantbooking.jwt.jwtservice.JwtClientService;
import com.application.restaurantbooking.jwt.jwtservice.JwtRestorerService;
import com.application.restaurantbooking.jwt.jwttoken.JwtTokenUtil;
import com.application.restaurantbooking.persistence.builder.ClientBuilder;
import com.application.restaurantbooking.persistence.builder.RestorerBuilder;
import com.application.restaurantbooking.persistence.model.Authority;
import com.application.restaurantbooking.persistence.model.Client;
import com.application.restaurantbooking.persistence.model.Restorer;
import com.application.restaurantbooking.persistence.repository.AuthorityRepository;
import com.application.restaurantbooking.persistence.service.ClientService;
import com.application.restaurantbooking.persistence.service.RestorerService;
import com.application.restaurantbooking.persistence.service.UserServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.Objects;

import static com.application.restaurantbooking.persistence.model.AuthorityName.*;

@RestController
public class AuthenticationController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private RestorerService restorerService;

    private ClientService clientService;

    private JwtRestorerService jwtRestorerService;

    private JwtClientService jwtClientService;

    private AuthorityRepository authorityRepository;

    private JwtTokenUtil jwtTokenUtil;

    private String exceptionText = "Bad credentials!";

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    BCryptPasswordEncoder bCryptPasswordEncoder,
                                    UserServiceManager userServiceManager,
                                    AuthorityRepository authorityRepository,
                                    JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.restorerService = userServiceManager.getRestorerService();
        this.clientService = userServiceManager.getClientService();
        this.jwtRestorerService = userServiceManager.getJwtRestorerService();
        this.jwtClientService = userServiceManager.getJwtClientService();
        this.authorityRepository = authorityRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(value = "${jwt.route.authentication.path}")
    public ResponseEntity<Object> createAuthenticationRestorerToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        UserDetails userDetails = jwtRestorerService.loadUserByUsername(authenticationRequest.getUsername());
        if (userDetails != null) {
            String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        } else {
            throw new AuthenticationException(exceptionText, new Exception());
        }
    }

    @PostMapping(value = "${jwt.route.authentication.client.path}")
    public ResponseEntity<Object> createAuthenticationClientToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        UserDetails userDetails = jwtClientService.loadUserByUsername(authenticationRequest.getUsername());
        if (userDetails != null) {
            String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtAuthenticationResponse(token));
        } else {
            throw new AuthenticationException(exceptionText, new Exception());
        }
    }

    @PostMapping(value = "${jwt.route.authentication.register}")
    public ResponseEntity<Object> registerRestorer(@RequestBody JwtAuthenticationRequest registrationRequest) {
        if (jwtRestorerService.loadUserByUsername(registrationRequest.getUsername()) != null) {
            throw new AuthenticationException("Username is unavailable!", null);
        } else {
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
                    return ResponseEntity.noContent().build();
                }
            } catch (BadCredentialsException e) {
                throw new AuthenticationException(exceptionText, e);
            } catch (Exception ex) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(registrationRequest);
        }
    }

    @PostMapping(value = "${jwt.route.authentication.client.register}")
    public ResponseEntity<Object> registerClient(@RequestBody JwtAuthenticationRequest registrationRequest) {
        if (jwtClientService.loadUserByUsername(registrationRequest.getUsername()) != null) {
            return ResponseEntity.noContent().build();
        } else {
            try {
                RegistrationValidation registrationValidation = new RegistrationValidation();
                if (registrationValidation.validate(registrationRequest)) {
                    LinkedList<Authority> authorityList = new LinkedList<>();
                    authorityList.add(authorityRepository.findByName(ROLE_CLIENT));
                    ClientBuilder clientBuilder = new ClientBuilder();
                    Client client = clientBuilder.username(registrationRequest.getUsername())
                            .password(bCryptPasswordEncoder.encode(registrationRequest.getPassword()))
                            .authorities(authorityList)
                            .build();
                    clientService.createClient(client);
                    return ResponseEntity.noContent().build();
                }
            } catch (Exception ex) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(registrationRequest);
        }
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException(exceptionText, e);
        }
    }
}
