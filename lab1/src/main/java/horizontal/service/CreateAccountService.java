package horizontal.service;

import horizontal.model.Bank;
import horizontal.model.accounts.Account;
import lombok.Data;

/**
 * Service class for creating accounts and registering them with a bank.
 */
@Data
public class CreateAccountService {

    /**
     * Creates a new account and registers it with the specified bank.
     *
     * @param account The account to create and register.
     * @param bank    The bank with which the account will be registered.
     */
    public void CreateAccount(Account account, Bank bank) {
        bank.accounts.registerAccount(account);
    }
}
