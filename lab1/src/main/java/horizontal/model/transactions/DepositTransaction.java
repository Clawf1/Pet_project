package horizontal.model.transactions;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents a deposit transaction.
 * Inherits from the Transaction class.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DepositTransaction extends Transaction {
    /**
     * Constructs a new DepositTransaction instance.
     *
     * @param bank    The UUID of the bank involved in the deposit transaction.
     * @param account The UUID of the account involved in the deposit transaction.
     * @param money   The amount of money deposited in the transaction.
     */
    public DepositTransaction(UUID bank, UUID account, BigDecimal money) {
        super(bank, account, money);
    }
}
