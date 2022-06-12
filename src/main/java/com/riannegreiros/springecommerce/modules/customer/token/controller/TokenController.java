package com.riannegreiros.springecommerce.modules.customer.token.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.riannegreiros.springecommerce.exception.APIException;
import com.riannegreiros.springecommerce.modules.customer.entity.Customer;
import com.riannegreiros.springecommerce.modules.customer.service.Impl.CustomerServiceImpl;
import com.riannegreiros.springecommerce.modules.customer.token.service.Impl.TokenServiceImpl;
import com.riannegreiros.springecommerce.utils.JWTConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/token")
public class TokenController {

    private final TokenServiceImpl tokenService;
    private final CustomerServiceImpl customerService;

    public TokenController(TokenServiceImpl tokenService, CustomerServiceImpl customerService) {
        this.tokenService = tokenService;
        this.customerService = customerService;
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        tokenService.confirmToken(token);
        return new ResponseEntity<>("Confirmed", HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public void refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith(JWTConstants.AUTH_TOKEN_TYPE)) {
            try {
                String refresh_token = authorizationHeader.substring(JWTConstants.AUTH_TOKEN_TYPE.length());
                Algorithm algorithm = Algorithm.HMAC512(JWTConstants.JWT_SECRET);
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String email = decodedJWT.getSubject();
                Customer customer = customerService.findByEmail(email);
                String access_token = JWT.create()
                        .withSubject(customer.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + JWTConstants.TOKEN_EXPIRATION))
                        .withIssuer(request.getRequestURL().toString())
                        .sign(Algorithm.HMAC512(JWTConstants.JWT_SECRET));
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                response.setHeader("ERROR", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new APIException(HttpStatus.BAD_REQUEST, "Refresh token is missing");
        }
    }
}
