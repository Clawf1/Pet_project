package horizontal.repository;

import horizontal.model.Bank;
import java.util.List;

/**
 * Interface for bank repositories.
 */
public interface IBankRepository extends IRepository<Bank> {

    /**
     * Registers a new bank.
     *
     * @param bank The bank to register.
     */
    void registerBank(Bank bank);

    /**
     * Retrieves all banks.
     *
     * @return A list of all banks.
     */
    List<Bank> getAllBanks();
}
