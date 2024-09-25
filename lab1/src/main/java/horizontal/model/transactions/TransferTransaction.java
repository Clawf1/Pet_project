package horizontal.model.transactions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents a transfer transaction.
 * Inherits from the Transaction class.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class TransferTransaction extends Transaction {
    /**
     * The UUID of the bank to which the transfer is being made.
     */
    private final UUID _to_bank;
    /**
     * The UUID of the account to which the transfer is being made.
     */
    private final UUID _to_account;

    /**
     * Constructs a new TransferTransaction instance.
     *
     * @param bank       The UUID of the bank involved in the transfer transaction.
     * @param account    The UUID of the account involved in the transfer transaction.
     * @param to_bank    The UUID of the bank to which the transfer is being made.
     * @param to_account The UUID of the account to which the transfer is being made.
     * @param money      The amount of money transferred in the transaction.
     */
    public TransferTransaction(UUID bank, UUID account, UUID to_bank, UUID to_account, BigDecimal money) {
        super(bank, account, money);
        _to_bank = to_bank;
        _to_account = to_account;
    }

    /**
     * Prints information about the transfer transaction, including the recipient bank and account.
     */
    public void print() {
        super.print();
        System.out.println("To bank: " + _to_bank);
        System.out.println("To account: " + _to_account);

    }
}
