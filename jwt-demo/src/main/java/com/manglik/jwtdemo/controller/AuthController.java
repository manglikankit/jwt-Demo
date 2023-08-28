package com.manglik.jwtdemo.controller;

import com.manglik.jwtdemo.Models.JwtRequest;
import com.manglik.jwtdemo.Models.JwtResponse;
import com.manglik.jwtdemo.Repository.UserRepository;
import com.manglik.jwtdemo.Security.JwtHelper;
import com.manglik.jwtdemo.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.manglik.jwtdemo.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
@Autowired
    private UserDetailsService userDetailsService;
@Autowired
    private AuthenticationManager authenticationManager;
@Autowired
    private JwtHelper jwtHelper;

@Autowired
    UserRepository userRepository;
@Autowired
    UserService userService;

private Logger logger = LoggerFactory.getLogger(AuthController.class);

@PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request){
        this.doAuthenticate(request.getEmail(), request.getPassword());
    UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
    String token = this.jwtHelper.generateToken(userDetails);
    JwtResponse response = JwtResponse.builder()
            .jwtToken(token)
            .username(userDetails.getUsername()).build();
    return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String pwd){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, pwd);
        try {
            authenticationManager.authenticate(authenticationToken);
        }
        catch (BadCredentialsException ex){
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandeler(){
        return "Invalid username or password";
    }

    @PostMapping("create-user")
    public User createUser(@RequestBody User user){

    return userService.createUser(user);
    }
}
