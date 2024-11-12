package horizontal.repository;

import horizontal.model.Client;

import java.util.List;

/**
 * Interface for client repositories.
 */
public interface IClientRepository extends IRepository<Client> {
    /**
     * Registers a new client.
     *
     * @param client The client to register.
     */
    void registerClient(Client client);

    /**
     * Retrieves all clients.
     *
     * @return A list of all clients.
     */
    List<Client> getAllClients();
}
