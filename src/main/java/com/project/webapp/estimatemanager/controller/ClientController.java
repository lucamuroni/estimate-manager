package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.ClientDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.exception.UserNotFoundException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/client")
//@CrossOrigin(origins = "*")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<ClientDto>> getAllClients() throws GenericException {
        try {
            List<ClientDto> clients = clientService.findAllClients();
            return new ResponseEntity<>(clients, HttpStatus.OK);
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping(value = "/find")
    public ResponseEntity<ClientDto> getClientById(@RequestParam("id") Long id) throws UserNotFoundException, GenericException {
        try {
            if (clientService.findClientById(id).isEmpty()) {
                throw new UserNotFoundException("Cliente assente o id errato");
            }
            ClientDto client = clientService.findClientById(id).get();
            return new ResponseEntity<>(client, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<ClientDto> addClient(@RequestBody ClientDto client) throws GenericException, NameAlreadyTakenException {
        try {
            if (clientService.findClientByEmail(client.getEmail()).isPresent())
                throw new NameAlreadyTakenException("Nome utente non disponibile");
            ClientDto newClient = clientService.addClient(client);
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
    public ResponseEntity<ClientDto> updateClient(@RequestBody ClientDto clientDto) throws UserNotFoundException, GenericException, NameAlreadyTakenException {
        try {
            if (clientService.findClientById(clientDto.getId()).isEmpty()) {
                throw new UserNotFoundException("Cliente assente o id errato");
            }
            ClientDto updateClient;
            updateClient = clientService.updateClient(clientDto);
            return new ResponseEntity<>(updateClient, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteClient(@RequestParam("id") Long id) throws UserNotFoundException, GenericException {
        try {
            if (clientService.findClientById(id).isEmpty()) {
                throw new UserNotFoundException("Cliente assente o id errato");
            }
            clientService.deleteClient(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }
}
