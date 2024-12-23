package horizontal.model.accounts;

import horizontal.model.Client;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Represents a debit account.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DebitAccount extends Account {
    /**
     * Constructs a new DebitAccount instance.
     * @param owner The client who owns the account.
     */
    public DebitAccount(Client owner) {
        super(owner);
    }

    /**
     * Subtracts money from the account.
     * @param money The amount of money to subtract.
     * @return True if the operation was successful, otherwise false.
     */
    @Override
    public boolean subtract(BigDecimal money) {
        if (get_money().compareTo(money) < 0) return false;
        set_money(get_money().subtract(money));
        return true;
    }
}
