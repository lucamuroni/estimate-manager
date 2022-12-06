package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.AuthResponseDto;
import com.project.webapp.estimatemanager.dtos.LoginDto;
import com.project.webapp.estimatemanager.dtos.UserDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.security.JWTGenerator;
import com.project.webapp.estimatemanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    //private final RoleService roleService;
    private final JWTGenerator jwtGenerator;


    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto user) throws NoSuchElementException, NameAlreadyTakenException, GenericException {
        try {
            UserDto newClient = userService.addUser(user);
            return new ResponseEntity<>(newClient, HttpStatus.CREATED);
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) throws NoSuchElementException, GenericException {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
            //UserDto user = userService.findUserByEmail(loginDto.getEmail());
            //return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }
}
