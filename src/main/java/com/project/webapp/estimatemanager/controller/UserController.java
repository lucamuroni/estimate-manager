package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.UserDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.exception.UserNotFoundException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/user")
//@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<UserDto>> getAllUsers() throws GenericException {
        try {
            List<UserDto> users = userService.findAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) throws NoSuchElementException, GenericException {
        try {
            UserDto user = userService.findUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<UserDto> addClient(@RequestBody UserDto user) throws GenericException, NameAlreadyTakenException {
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

    @PutMapping(value = "/update")
    public ResponseEntity<UserDto> updateClient(@RequestBody UserDto userDto) throws NoSuchElementException, GenericException, NameAlreadyTakenException {
        try {
            UserDto updateUser = userService.updateUser(userDto);
            return new ResponseEntity<>(updateUser, HttpStatus.OK);
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable("id") Long id) throws NoSuchElementException, GenericException {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }
}
