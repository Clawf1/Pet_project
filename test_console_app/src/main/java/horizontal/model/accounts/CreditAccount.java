package horizontal.model.accounts;

import horizontal.model.Client;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Represents a credit account.
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CreditAccount extends Account {
    private BigDecimal _limit;

    /**
     * Constructs a new CreditAccount instance.
     * @param owner The client who owns the account.
     * @param limit The credit limit of the account.
     */
    public CreditAccount(Client owner, BigDecimal limit) {
        super(owner);
        _limit = limit;
    }

    /**
     * Subtracts money from the account.
     * @param money The amount of money to subtract.
     * @return True if the operation was successful, otherwise false.
     */
    @Override
    public boolean subtract(BigDecimal money) {
        if (get_money().subtract(money).compareTo(_limit) < 0)
            return false;
        set_money(get_money().subtract(money));
        return true;
    }

    /**
     * Changes the cashback based on a percentage.
     * @param percent The percentage to apply.
     */
    public void ChangeCashback(BigDecimal percent) {
        if (get_money().compareTo(BigDecimal.valueOf(0)) < 0)
            addCashback((get_money().multiply(percent)).negate());
    }

    /**
     * Accrues cashback for the account.
     */
    public void accrue() {
        set_money(get_money().subtract(get_cashback()));
        if (get_money().compareTo(_limit) < 0)
            set_money(get_limit());
    }
}
