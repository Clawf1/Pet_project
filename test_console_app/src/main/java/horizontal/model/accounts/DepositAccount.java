package horizontal.model.accounts;

import horizontal.model.Client;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a deposit account.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DepositAccount extends Account {
    private BigDecimal _sum;
    private BigDecimal _percent;
    private LocalDate _until_date;
    private boolean opened;

    /**
     * Constructs a new DepositAccount instance.
     * @param owner The client who owns the account.
     * @param money The initial amount of money in the account.
     * @param percent The interest rate percentage for the deposit.
     * @param until The date until which the deposit is valid.
     * @throws IllegalArgumentException if money, percent, or until are null, or if money or percent are not positive.
     */
    public DepositAccount(Client owner, BigDecimal money, BigDecimal percent, LocalDate until) {
        super(owner);
        if (money == null || percent == null || until == null) {
            throw new IllegalArgumentException("Money, percent, and until date must not be null.");
        }
        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Money must be a positive value.");
        }
        if (percent.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Percent must be a positive value.");
        }
        _sum = money;
        _percent = percent;
        _until_date = until;
        opened = true;
    }

    /**
     * Changes the date of the account.
     * @param cur_date The current date.
     */
    void ChangeDate(LocalDate cur_date) {
        if (!_until_date.isAfter(cur_date)) opened = true;
    }

    /**
     * Subtracts money from the account.
     * @param money The amount of money to subtract.
     * @return True if the operation was successful, otherwise false.
     */
    @Override
    public boolean subtract(BigDecimal money) {
        if (!opened || get_money().compareTo(money) < 0) return false;
        set_money(get_money().subtract(money));
        return true;
    }
}
