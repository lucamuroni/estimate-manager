package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.ClientDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.models.Client;
import com.project.webapp.estimatemanager.repository.ClientRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class ClientService {
    private final ClientRepo clientRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientService(ClientRepo clientRepo, ModelMapper modelMapper) {
        this.clientRepo = clientRepo;
        this.modelMapper = modelMapper;
    }

    public ClientDto addClient(ClientDto clientDto) throws Exception {
        try {
            Client client = this.saveChanges(clientDto);
            clientRepo.save(client);
            return clientRepo.findClientByEmail(client.getEmail()).stream()
                    .map(source -> modelMapper.map(source, ClientDto.class))
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

    public ClientDto updateClient(ClientDto clientDto) throws Exception {
        try {
            Client client = clientRepo.findClientById(clientDto.getId()).orElseThrow();
            Client modifiedClient = this.saveChanges(clientDto, client);
            clientRepo.save(modifiedClient);
            return clientRepo.findClientById(modifiedClient.getId()).stream()
                    .map(source -> modelMapper.map(source, ClientDto.class))
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

    public List<ClientDto> findAllClients() throws Exception {
        try {
            List<Client> clients = clientRepo.findAll();
            return clients.stream()
                    .map(source -> modelMapper.map(source, ClientDto.class))
                    .toList();
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public Optional<ClientDto> findClientByEmail(String email) throws Exception {
        try {
            Optional<Client> client = clientRepo.findClientByEmail(email);
            return client.stream()
                    .map(source -> modelMapper.map(source, ClientDto.class))
                    .findFirst();
        } catch (NullPointerException e) {
            throw new Exception("Nessun elemento nella lista");
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public Optional<ClientDto> findClientById(Long id) throws Exception {
        try {
            Optional<Client> client = clientRepo.findClientById(id);
            return client.stream()
                    .map(source -> modelMapper.map(source, ClientDto.class))
                    .findFirst();
        } catch (NullPointerException e) {
            throw new Exception("Nessun elemento nella lista");
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public void deleteClient(Long id) throws Exception {
        try {
            clientRepo.deleteClientById(id);
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    private Client saveChanges(ClientDto clientDto) {
        Client client = new Client();
        client.setEmail(clientDto.getEmail());
        client.setName(clientDto.getName());
        client.setPassword(clientDto.getPassword());
        return client;
    }

    private Client saveChanges(ClientDto clientDto, Client client) throws NameAlreadyTakenException, GenericException {
        try {
            if (!clientDto.getEmail().equals(client.getEmail())) {
                if (clientRepo.findClientByEmail(clientDto.getEmail()).isPresent()) {
                    throw new NameAlreadyTakenException("Nuovo nome utente non disponibile, ritentare");
                }
                client.setEmail(clientDto.getEmail());
            }
            client.setName(clientDto.getName());
            client.setPassword(clientDto.getPassword());
            return client;
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException("Problema sconosciuto");
        }
    }
}
