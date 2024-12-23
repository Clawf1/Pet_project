package horizontal.service;

import horizontal.model.Bank;
import horizontal.repository.BankRepository;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Service class for managing operations related to the central bank.
 */
@Data
public class CentralBankService {

    private int _beginning_of_month = LocalDate.now().getDayOfMonth();
    private LocalDate _current_date = LocalDate.now();

    private BankRepository _banks;

    /**
     * Constructs a CentralBankService with the specified bank repository.
     *
     * @param banks The bank repository to be managed by the central bank service.
     */
    public CentralBankService(BankRepository banks) {
        _banks = banks;
    }

    /**
     * Registers a bank with the central bank.
     *
     * @param bank The bank to be registered.
     */
    public void RegisterBank(Bank bank) {
        _banks.registerBank(bank);
    }

    /**
     * Skips time to the specified date.
     *
     * @param time The date to skip time to.
     */
    public void SkipTimeTo(LocalDate time) {
        var days = ChronoUnit.DAYS.between(_current_date, time);
        SkipDays(days);
    }

    /**
     * Skips the specified number of days.
     *
     * @param days The number of days to skip.
     */
    public void SkipDays(long days) {
        while (days-- > 0) {
            _current_date = _current_date.plusDays(1);
            _banks.SkipDay(_current_date.getDayOfMonth() == _beginning_of_month);
        }
    }
}
