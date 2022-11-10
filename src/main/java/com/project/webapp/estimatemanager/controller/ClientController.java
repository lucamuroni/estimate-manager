package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.ClientDto;
import com.project.webapp.estimatemanager.exception.UserNotFoundException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//TODO: inserire tutti i try catch
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
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> clients = clientService.findAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping(value = "/find")
    public ResponseEntity<ClientDto> getClientById(@RequestParam("id") Long id) throws UserNotFoundException {
        if (clientService.findClientById(id).isEmpty()) {
            throw new UserNotFoundException("Cliente assente o id errato");
        }
        ClientDto client = clientService.findClientById(id).get();
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<ClientDto> addClient(@RequestBody ClientDto client) throws NameAlreadyTakenException {
        if (clientService.findClientByEmail(client.getEmail()).isPresent())
            throw new NameAlreadyTakenException("Nome utente non disponibile");
        ClientDto newClient = clientService.addClient(client);
        return new ResponseEntity<>(newClient, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<ClientDto> updateClient(@RequestBody ClientDto clientDto) throws UserNotFoundException, NameAlreadyTakenException {
        if (clientService.findClientById(clientDto.getId()).isEmpty()) {
            throw new UserNotFoundException("Cliente assente o id errato");
        }
        ClientDto updateClient;
        try {
            updateClient = clientService.updateClient(clientDto);
        } catch (Exception e) {
            throw new NameAlreadyTakenException(e.getMessage());
        }
        return new ResponseEntity<>(updateClient, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteClient(@RequestParam("id") Long id) throws UserNotFoundException {
        if (clientService.findClientById(id).isEmpty()) {
            throw new UserNotFoundException("Cliente assente o id errato");
        }
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
