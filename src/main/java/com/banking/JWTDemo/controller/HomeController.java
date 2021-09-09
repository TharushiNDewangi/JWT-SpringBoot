package com.banking.JWTDemo.controller;

import com.banking.JWTDemo.model.JwtRequest;
import com.banking.JWTDemo.model.JwtResponse;
import com.banking.JWTDemo.service.UserService;
import com.banking.JWTDemo.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    //meka enna klin spring security walin authenticate wenna ona
    //mekata awa details illanwa dummy (userge username and pw)
    //apita meke inne predefine krpu admin ne ekama user
    @GetMapping("/")
    public String home() {
        return "Hi I'm Dew This is my First time in use JWT Token in Spring boot. " +
                "I wish it will going better";
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{
//get details uname pw from model for authentication
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
//dn token hadanna ona ita klin serdetail object ek hadannaona
        //username eken spring security
        final UserDetails userDetails
                = userService.loadUserByUsername(jwtRequest.getUsername());
//create token userdetail input parameter widihata danawa
        final String token =
                jwtUtility.generateToken(userDetails);
//pass krnw response ek
        return  new JwtResponse(token);
    }
}