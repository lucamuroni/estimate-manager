package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.RoleDto;
import com.project.webapp.estimatemanager.dtos.UserDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.models.Role;
import com.project.webapp.estimatemanager.models.UserEntity;
import com.project.webapp.estimatemanager.repository.RoleRepo;
import com.project.webapp.estimatemanager.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepo userRepo, RoleRepo roleRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.modelMapper = modelMapper;
    }

    public UserDto addUser(UserDto userDto) throws Exception {
        try {
            UserEntity user = this.saveChanges(userDto);
            userRepo.save(user);
            return userRepo
                    .findUserEntityByEmail(user.getEmail()).stream()
                    .map(source -> modelMapper.map(source, UserDto.class))
                    .findFirst()
                    .orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Nessun elemento nella lista");
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public UserDto updateUser(UserDto userDto) throws Exception {
        try {
            UserEntity user = userRepo.findUserEntityById(userDto.getId()).orElseThrow();
            UserEntity modifiedClient = this.saveChanges(userDto, user);
            userRepo.save(modifiedClient);
            return userRepo.findUserEntityById(modifiedClient.getId())
                    .stream()
                    .map(source -> modelMapper.map(source, UserDto.class))
                    .findFirst()
                    .orElseThrow();
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Nessun elemento nella lista");
        } catch (GenericException e) {
            throw new GenericException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public List<UserDto> findAllUsers() throws Exception {
        try {
            List<UserEntity> users = userRepo.findAll();
            return users
                    .stream()
                    .map(source -> modelMapper.map(source, UserDto.class))
                    .toList();
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public UserDto findUserById(Long id) throws Exception {
        try {
            Optional<UserEntity> user = userRepo.findById(id);
            if (user.isPresent())
                return user
                        .stream()
                        .map(source ->modelMapper.map(source, UserDto.class))
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

    public UserDto findUserByEmail(String email) throws Exception {
        try {
            Optional<UserEntity> user = userRepo.findUserEntityByEmail(email);
            if (user.isPresent())
                return user.stream()
                        .map(source ->modelMapper.map(source, UserDto.class))
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

    public void deleteUser(Long id) throws Exception {
        try {
            if (userRepo.findUserEntityById(id).isPresent())
                userRepo.deleteUserEntityById(id);
            else
                throw new NoSuchElementException("Nessun elemento trovato");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    private UserEntity saveChanges(UserDto userDto) throws Exception {
        try {
            if (userRepo.findUserEntityByEmail(userDto.getEmail()).isPresent())
                throw new NameAlreadyTakenException("Nome utente non disponibile");
            else {
                UserEntity user = new UserEntity();
                user.setName(userDto.getName());
                user.setEmail(userDto.getEmail());
                user.setPassword(userDto.getPassword());
                user.setRoles(this.checkRoles(userDto.getRoles()));
                return user;
            }
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private UserEntity saveChanges(UserDto userDto, UserEntity user) throws NameAlreadyTakenException, GenericException {
        try {
            if (userDto.getEmail().equals(user.getEmail())) {
                if (userRepo.findUserEntityByEmail(userDto.getEmail()).isPresent()) {
                    throw new NameAlreadyTakenException("Nome utente non disponibile");
                }
                user.setEmail(userDto.getEmail());
            }
            user.setName(userDto.getName());
            user.setPassword(userDto.getPassword());
            return user;
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException("Problema sconosciuto");
        }
    }

    private Set<Role> checkRoles(Set<RoleDto> roleDto) throws NoSuchElementException, GenericException {
        try {
            Set<Role> roles = new HashSet<>();
            for (RoleDto role : roleDto) {
                if (roleRepo.findById(role.getId()).isPresent())
                    roles.add(roleRepo.findById(role.getId()).get());
                else
                    throw new NoSuchElementException("Ruolo non trovato");
            }
            return roles;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException("Problema sconosciuto");
        }
    }
}
