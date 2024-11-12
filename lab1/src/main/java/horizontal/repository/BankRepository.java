package horizontal.repository;

import horizontal.model.Bank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing banks.
 */
public class BankRepository implements IBankRepository {
    private final List<Bank> banks;

    /**
     * Constructs a new BankRepository.
     */
    public BankRepository() {
        this.banks = new ArrayList<>();
    }

    /**
     * Registers a new bank.
     *
     * @param bank The bank to register.
     */
    @Override
    public void registerBank(Bank bank) {
        if (banks.contains(bank)) return;
        while (findById(bank.get_id()).isPresent())
            bank.set_id(UUID.randomUUID());
        banks.add(bank);
    }

    /**
     * Finds a bank by ID.
     *
     * @param id The ID of the bank to find.
     * @return An Optional containing the found bank, or empty if not found.
     */
    @Override
    public Optional<Bank> findById(UUID id) {
        return banks.stream().filter(b -> b.get_id().equals(id)).findAny();
    }

    /**
     * Retrieves all banks.
     *
     * @return A list of all banks.
     */
    @Override
    public List<Bank> getAllBanks() {
        return banks;
    }

    /**
     * Skips a day for all banks and accrues cashback if specified.
     *
     * @param accrue_day_flag Flag indicating the day of the month when cashback is accrued.
     */
    public void SkipDay(boolean accrue_day_flag) {
        for (var bank : banks) {
            bank.SkipDay();
            if (accrue_day_flag) bank.AccrueCashback();
        }
    }
}
