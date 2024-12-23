package horizontal.model.transactions;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents a withdrawal transaction.
 * Inherits from the Transaction class.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WithdrawalTransaction extends Transaction {
    /**
     * Constructs a new WithdrawalTransaction instance.
     *
     * @param bank    The UUID of the bank involved in the withdrawal transaction.
     * @param account The UUID of the account involved in the withdrawal transaction.
     * @param money   The amount of money withdrawn in the transaction.
     */
    public WithdrawalTransaction(UUID bank, UUID account, BigDecimal money) {
        super(bank, account, money);
    }
}
