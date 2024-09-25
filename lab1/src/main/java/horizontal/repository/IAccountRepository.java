package horizontal.repository;

import horizontal.model.accounts.Account;
import java.util.List;

/**
 * Interface for account repositories.
 */
public interface IAccountRepository extends IRepository<Account> {

    /**
     * Registers a new account.
     *
     * @param account The account to register.
     */
    void registerAccount(Account account);

    /**
     * Retrieves all accounts.
     *
     * @return A list of all accounts.
     */
    List<Account> getAllAccounts();
}
