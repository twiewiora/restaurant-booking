package com.application.restaurantbooking.controllers;

import com.application.restaurantbooking.controllers.dto.ClientDTO;
import com.application.restaurantbooking.jwt.jwttoken.JwtTokenUtil;
import com.application.restaurantbooking.persistence.model.Client;
import com.application.restaurantbooking.persistence.model.Restorer;
import com.application.restaurantbooking.persistence.service.ClientService;
import com.application.restaurantbooking.persistence.service.RestorerService;
import com.application.restaurantbooking.persistence.service.UserServiceManager;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

@RestController
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Value("${jwt.header}")
    private String tokenHeader;

    private JwtTokenUtil jwtTokenUtil;

    private ClientService clientService;

    private RestorerService restorerService;

    private UserServiceManager userServiceManager;

    private ModelMapper modelMapper;

    @Autowired
    public UserController(JwtTokenUtil jwtTokenUtil,
                          ClientService clientService,
                          RestorerService restorerService,
                          UserServiceManager userServiceManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.clientService = clientService;
        this.restorerService = restorerService;
        this.userServiceManager = userServiceManager;
        this.modelMapper = new ModelMapper();
    }

    @ApiOperation(value = "Validate restorer's token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Correct validation"),
            @ApiResponse(code = 401, message = "Incorrect token"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.VALIDATE_RESTORER, produces = "application/json; charset=UTF-8")
    public void validateRestorer(HttpServletRequest request, HttpServletResponse response) {
        if (request.getHeader(tokenHeader) != null) {
            String token = request.getHeader(tokenHeader).substring(7);
            try {
                String username = jwtTokenUtil.getUsernameFromToken(token);
                if (restorerService.getByUsername(username) != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    return;
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @ApiOperation(value = "Validate client's token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Correct validation"),
            @ApiResponse(code = 401, message = "Incorrect token"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.VALIDATE_CLIENT, produces = "application/json; charset=UTF-8")
    public void validateClient(HttpServletRequest request, HttpServletResponse response) {
        if (request.getHeader(tokenHeader) != null) {
            String token = request.getHeader(tokenHeader).substring(7);
            try {
                String username = jwtTokenUtil.getUsernameFromToken(token);
                if (clientService.getByUsername(username) != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    return;
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @ApiOperation(value = "Get client by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Client details"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
    })
    @GetMapping(value = UrlRequests.GET_CLIENT_BY_ID, produces = "application/json; charset=UTF-8")
    public ClientDTO getClientById(HttpServletRequest request, HttpServletResponse response,
                                @PathVariable String id) {
        Restorer restorer = userServiceManager.getRestorerByJwt(request);
        if (restorer == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Client client = clientService.getById(Long.decode(id));
        if (client != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            return modelMapper.map(client, ClientDTO.class);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }
    }

}
