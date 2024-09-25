package horizontal.repository;

import horizontal.model.Client;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing clients.
 */
public class ClientRepository implements IClientRepository {
    private final List<Client> clients;

    /**
     * Constructs a new ClientRepository.
     */
    public ClientRepository() {
        this.clients = new ArrayList<>();
    }

    /**
     * Registers a new client.
     *
     * @param client The client to register.
     */
    @Override
    public void registerClient(Client client) {
        if (clients.contains(client)) return;
        while (findById(client.get_id()).isPresent())
            client.set_id(UUID.randomUUID());
        clients.add(client);
    }

    /**
     * Finds a client by ID.
     *
     * @param id The ID of the client to find.
     * @return An Optional containing the found client, or empty if not found.
     */
    @Override
    public Optional<Client> findById(UUID id) {
        return clients.stream()
                .filter(c -> c.get_id().equals(id))
                .findAny();
    }

    /**
     * Retrieves all clients.
     *
     * @return A list of all clients.
     */
    @Override
    public List<Client> getAllClients() {
        return clients;
    }
}
