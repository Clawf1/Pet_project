package horizontal.repository;

import horizontal.model.accounts.Account;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing accounts.
 */
public class AccountRepository implements IAccountRepository {
    private final List<Account> accounts;

    /**
     * Constructs a new AccountRepository.
     */
    public AccountRepository() {
        this.accounts = new ArrayList<>();
    }

    /**
     * Registers a new account.
     *
     * @param account The account to register.
     */
    @Override
    public void registerAccount(Account account) {
        if (accounts.contains(account)) return;
        while (findById(account.get_id()).isPresent())
            account.set_id(UUID.randomUUID());
        accounts.add(account);
    }

    /**
     * Finds an account by ID.
     *
     * @param id The ID of the account to find.
     * @return An Optional containing the found account, or empty if not found.
     */
    @Override
    public Optional<Account> findById(UUID id) {
        return accounts.stream()
                .filter(u -> u.get_id().equals(id))
                .findAny();
    }

    /**
     * Retrieves all accounts.
     *
     * @return A list of all accounts.
     */
    @Override
    public List<Account> getAllAccounts() {
        return accounts;
    }

    /**
     * Displays all accounts.
     */
    public void ShowAllAccounts() {
        if (accounts.isEmpty()) System.out.println("No accounts found");
        for (Account account : accounts)
            account.print();
    }
}
