package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.RoleDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.models.Role;
import com.project.webapp.estimatemanager.repository.RoleRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class RoleService {
    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public RoleService(RoleRepo roleRepo, ModelMapper modelMapper) {
        this.roleRepo = roleRepo;
        this.modelMapper = modelMapper;
    }

    public RoleDto addRole(RoleDto roleDto) throws Exception {
        try {
            Role role = this.saveChanges(roleDto);
            Optional<Role> savedRole = Optional.of(roleRepo.save(role));
            return savedRole
                    .stream()
                    .map(source -> modelMapper.map(source, RoleDto.class))
                    .findFirst()
                    .orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Nessun elemento nella lista");
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public RoleDto updateRole(RoleDto roleDto) throws Exception {
        try {
            Role role = roleRepo.findRoleById(roleDto.getId()).orElseThrow(() -> new NoSuchElementException("Ruolo non trovato"));
            Role modifiedRole = this.saveChanges(roleDto, role);
            Optional<Role> savedRole = Optional.of(roleRepo.save(modifiedRole));
            return savedRole
                    .stream()
                    .map(source -> modelMapper.map(source, RoleDto.class))
                    .findFirst()
                    .orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Nessun elemento nella lista");
        } catch (GenericException e) {
            throw new GenericException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public List<RoleDto> findAllRoles() throws Exception {
        try {
            List<Role> roles = roleRepo.findAll();
            return roles
                    .stream()
                    .map(source -> modelMapper.map(source, RoleDto.class))
                    .toList();
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public RoleDto findRoleById(Long id) throws Exception {
        try {
            Optional<Role> role = roleRepo.findRoleById(id);
            if (role.isPresent())
                return role
                        .stream()
                        .map(source -> modelMapper.map(source, RoleDto.class))
                        .findFirst()
                        .get();
            else
                throw new NoSuchElementException("Nessun elemento trovato");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public void deleteRole(Long id) throws Exception {
        try {
            if (roleRepo.findRoleById(id).isPresent())
                roleRepo.deleteRoleById(id);
            else
                throw new NoSuchElementException("Nessun elemento trovato");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    private Role saveChanges(RoleDto roleDto) throws Exception {
        try {
            if (roleRepo.findRoleByName(roleDto.getName()).isPresent())
                throw new NameAlreadyTakenException("Nome ruolo non disponibile");
            else {
                Role role = new Role();
                role.setName(roleDto.getName());
                return role;
            }
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    private Role saveChanges(RoleDto roleDto, Role role) throws NameAlreadyTakenException, GenericException {
        try {
            if (!roleDto.getName().equals(role.getName())) {
                if (roleRepo.findRoleByName(roleDto.getName()).isPresent())
                    throw new NameAlreadyTakenException("Ruolo con quel nome già esistente");
                else
                    role.setName(roleDto.getName());
            }
            return role;
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException("Problema sconosciuto");
        }
    }
}
