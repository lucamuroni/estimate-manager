package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.models.Client;
import com.project.webapp.estimatemanager.repository.ClientRepo;
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

    @Autowired
    public ClientService(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    public Client addClient(Client client) {
        return clientRepo.save(client);
    }

    public List<Client> findAllClients() {
        return clientRepo.findAll();
    }

    public Client updateClient(Client client) {
        return clientRepo.save(client);
    }

    public Optional<Client> findClientByEmail(String email) {
        return clientRepo.findClientByEmail(email);
    }

    public void deleteClient(String email) {
        clientRepo.deleteClientByEmail(email);
    }
}
