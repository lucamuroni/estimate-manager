package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.ClientDto;
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
    public ResponseEntity<ClientDto> getClientByEmail(@RequestParam("email") String email) {
        if (clientService.findClientByEmail(email).isPresent()) {
            ClientDto client = clientService.findClientByEmail(email).get();
            return new ResponseEntity<>(client, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<ClientDto> addClient(@RequestBody ClientDto client) {
        if (clientService.findClientByEmail(client.getEmail()).isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        ClientDto newClient = clientService.addClient(client);
        return new ResponseEntity<>(newClient, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<ClientDto> updateClient(@RequestBody ClientDto clientDto) {
        if (clientService.findClientById(clientDto.getId()).isPresent()) {
            ClientDto updateClient = clientService.updateClient(clientDto);
            return new ResponseEntity<>(updateClient, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteClient(@RequestParam("id") Long id) {
        if (clientService.findClientById(id).isPresent()) {
            clientService.deleteClient(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
