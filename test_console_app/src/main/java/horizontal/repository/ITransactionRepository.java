package horizontal.repository;

import horizontal.model.transactions.Transaction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for transaction repositories.
 */
public interface ITransactionRepository extends IRepository<Transaction> {

    /**
     * Adds a new transaction to the repository.
     *
     * @param transaction The transaction to add.
     */
    void addTransaction(Transaction transaction);

    /**
     * Removes a transaction from the repository by its ID.
     *
     * @param id The ID of the transaction to remove.
     */
    void removeTransaction(UUID id);

    /**
     * Finds a transaction in the repository.
     *
     * @param transaction The transaction to find.
     * @return An Optional containing the found transaction, or empty if not found.
     */
    Optional<Transaction> findTransaction(Transaction transaction);

    /**
     * Retrieves all transactions in the repository.
     *
     * @return A list of all transactions.
     */
    List<Transaction> getAllTransactions();
}
