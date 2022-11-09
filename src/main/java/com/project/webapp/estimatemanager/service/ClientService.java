package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.ClientDto;
import com.project.webapp.estimatemanager.models.Client;
import com.project.webapp.estimatemanager.repository.ClientRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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

    public void addClient(ClientDto clientDto) {
        Client client = new Client();
        client.setEmail(clientDto.getEmail());
        client.setName(clientDto.getName());
        client.setPassword(clientDto.getPassword());
        clientRepo.save(client);
    }

    public List<ClientDto> findAllClients() {
        List<Client> clients = clientRepo.findAll();
        return clients.stream()
                .map(source -> modelMapper.map(source, ClientDto.class))
                .toList();
    }

    public Client updateClient(Client client) {
        return clientRepo.save(client);
    }

    public Optional<ClientDto> findClientByEmail(String email) {
        Optional<Client> client = clientRepo.findClientByEmail(email);
        return client.stream()
                .map(source -> modelMapper.map(source, ClientDto.class))
                .findFirst();
    }

    public void deleteClient(String email) {
        clientRepo.deleteClientByEmail(email);
    }
}
