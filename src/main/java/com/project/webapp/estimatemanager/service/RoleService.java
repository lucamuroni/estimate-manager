package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.RoleDto;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.models.Role;
import com.project.webapp.estimatemanager.repository.RoleRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

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
            roleRepo.save(role);
            return roleRepo
                    .findRoleByName(role.getName())
                    .stream()
                    .map(source -> modelMapper.map(source, RoleDto.class))
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
}
