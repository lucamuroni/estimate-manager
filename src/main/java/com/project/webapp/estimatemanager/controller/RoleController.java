package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.RoleDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/role")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<RoleDto>> getAllRoles() throws GenericException {
        try {
            List<RoleDto> roles = roleService.findAllRoles();
            return new ResponseEntity<>(roles, HttpStatus.OK);
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable("id") Long id) throws NoSuchElementException, GenericException {
        try {
            RoleDto role = roleService.findRoleById(id);
            return new ResponseEntity<>(role, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<RoleDto> addRole(@RequestBody RoleDto roleDto) throws GenericException, NameAlreadyTakenException, NoSuchElementException {
        try {
            RoleDto newRole = roleService.addRole(roleDto);
            return new ResponseEntity<>(newRole, HttpStatus.OK);
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<RoleDto> updateRole(@RequestBody RoleDto roleDto) throws GenericException, NameAlreadyTakenException, NoSuchElementException {
        try {
            RoleDto updateRole = roleService.updateRole(roleDto);
            return new ResponseEntity<>(updateRole, HttpStatus.OK);
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable("id") Long id) throws GenericException, NoSuchElementException {
        try {
            roleService.deleteRole(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }
}
