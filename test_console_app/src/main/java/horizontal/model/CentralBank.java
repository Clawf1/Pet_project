package horizontal.model;

import lombok.Data;
import horizontal.repository.IBankRepository;

/**
 * Represents a central bank in the system.
 */
@Data
public class CentralBank {
    private IBankRepository _banks;

    /**
     * Adds a bank to the central bank's repository.
     * @param bank The bank to be added.
     */
    public void AddBank(Bank bank) {
        _banks.registerBank(bank);
    }
}