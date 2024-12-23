package horizontal.model.accounts;

import horizontal.model.Client;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents an abstract bank account.
 */
@Data
public abstract class Account {
    private UUID _id = UUID.randomUUID();
    private BigDecimal _money = BigDecimal.ZERO;
    private BigDecimal _cashback = BigDecimal.ZERO;

    private final Client _owner;

    /**
     * Adds money to the account.
     * @param money The amount of money to add.
     */
    public void add(BigDecimal money) {
        _money = _money.add(money);
    }

    /**
     * Adds cashback to the account.
     * @param cash The amount of cashback to add.
     */
    public void addCashback(BigDecimal cash) {
        _cashback = _cashback.add(cash);
    }

    /**
     * Subtracts money from the account.
     * @param money The amount of money to subtract.
     * @return True if the operation was successful, otherwise false.
     */
    public abstract boolean subtract(BigDecimal money);

    /**
     * Accrues cashback for the account.
     */
    public void accrue() {
        _money = _money.add(_cashback);
        _cashback = BigDecimal.valueOf(0);
    }

    /**
     * Changes the cashback based on a percentage.
     * @param percent The percentage to apply.
     */
    public void ChangeCashback(BigDecimal percent) {
        addCashback(get_money().multiply(percent));
    }

    /**
     * Prints information about the account.
     */
    public void print() {
        System.out.println("Account ID: " + _id);
        System.out.println("Owner name: " + _owner.get_name());
        System.out.println("Owner ID: " + _owner.get_id());
    }
}
