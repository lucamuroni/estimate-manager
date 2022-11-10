package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.ClientDto;
import com.project.webapp.estimatemanager.models.Client;
import com.project.webapp.estimatemanager.repository.ClientRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

//TODO: inserire tutti i try catch
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

    public ClientDto addClient(ClientDto clientDto) {
        Client client = this.saveChanges(clientDto);
        clientRepo.save(client);
        return clientRepo.findClientByEmail(client.getEmail()).stream()
                .map(source -> modelMapper.map(source, ClientDto.class))
                .findFirst()
                .get();
    }

    public ClientDto updateClient(ClientDto clientDto) throws Exception {
        try {
            Client client = clientRepo.findClientById(clientDto.getId()).get();
            Client modifiedClient = this.saveChanges(clientDto, client);
            clientRepo.save(modifiedClient);
            return clientRepo.findClientById(modifiedClient.getId()).stream()
                    .map(source -> modelMapper.map(source, ClientDto.class))
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            throw new Exception("Dato non trovato");
        }
    }

    public List<ClientDto> findAllClients() {
        List<Client> clients = clientRepo.findAll();
        return clients.stream()
                .map(source -> modelMapper.map(source, ClientDto.class))
                .toList();
    }

    public Optional<ClientDto> findClientByEmail(String email) {
        Optional<Client> client = clientRepo.findClientByEmail(email);
        return client.stream()
                .map(source -> modelMapper.map(source, ClientDto.class))
                .findFirst();
    }

    public Optional<ClientDto> findClientById(Long id) {
        Optional<Client> client = clientRepo.findClientById(id);
        return client.stream()
                .map(source -> modelMapper.map(source, ClientDto.class))
                .findFirst();
    }

    public void deleteClient(Long id) {
        clientRepo.deleteClientById(id);
    }

    private Client saveChanges(ClientDto clientDto) {
        Client client = new Client();
        client.setEmail(clientDto.getEmail());
        client.setName(clientDto.getName());
        client.setPassword(clientDto.getPassword());
        return client;
    }

    private Client saveChanges(ClientDto clientDto, Client client) throws Exception {
        if (!clientDto.getEmail().equals(client.getEmail())) {
            if (clientRepo.findClientByEmail(clientDto.getEmail()).isPresent()) {
                throw new Exception("Nuovo nome utente non disponibile, ritentare");
            }
            client.setEmail(clientDto.getEmail());
        }
        client.setName(clientDto.getName());
        client.setPassword(clientDto.getPassword());
        return client;
    }
}
