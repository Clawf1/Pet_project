package horizontal.repository;

import horizontal.model.transactions.Transaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing transactions.
 */
public class TransactionRepository implements ITransactionRepository {

    private final List<Transaction> transactions;

    /**
     * Constructs a new TransactionRepository.
     */
    public TransactionRepository() {
        this.transactions = new ArrayList<>();
    }

    /**
     * Adds a new transaction.
     *
     * @param transaction The transaction to add.
     */
    @Override
    public void addTransaction(Transaction transaction) {
        if (transactions.contains(transaction)) return;
        while (findById(transaction.get_id()).isPresent())
            transaction.set_id(UUID.randomUUID());
        transactions.add(transaction);
    }

    /**
     * Removes a transaction by ID.
     *
     * @param id The ID of the transaction to remove.
     */
    public void removeTransaction(UUID id) {
        transactions.removeIf(t -> t.get_id().equals(id));
    }

    /**
     * Finds a transaction.
     *
     * @param transaction The transaction to find.
     * @return An Optional containing the found transaction, or empty if not found.
     */
    @Override
    public Optional<Transaction> findTransaction(Transaction transaction) {
        return transactions.stream().filter(t -> t.equals(transaction)).findAny();
    }

    /**
     * Finds a transaction by ID.
     *
     * @param id The ID of the transaction to find.
     * @return An Optional containing the found transaction, or empty if not found.
     */
    @Override
    public Optional<Transaction> findById(UUID id) {
        return transactions.stream().filter(t -> t.get_id().equals(id)).findAny();
    }

    /**
     * Retrieves all transactions.
     *
     * @return A list of all transactions.
     */
    @Override
    public List<Transaction> getAllTransactions() {
        return transactions;
    }
}
