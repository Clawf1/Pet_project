package horizontal.model.transactions;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a transaction.
 */
@Data
@EqualsAndHashCode
public abstract class Transaction {
    private UUID _id = UUID.randomUUID();
    private final UUID _bank;
    private final UUID _account;
    private final BigDecimal _money;
    private final LocalDate _date;

    /**
     * Constructs a new Transaction instance.
     * @param bank The UUID of the bank involved in the transaction.
     * @param account The UUID of the account involved in the transaction.
     * @param money The amount of money involved in the transaction.
     */
    public Transaction(UUID bank, UUID account, BigDecimal money) {
        _date = LocalDate.now();
        _bank = bank;
        _account = account;
        _money = money;
    }

    /**
     * Prints information about the transaction.
     */
    public void print() {
        System.out.println("ID: " + _id);
        System.out.println("Bank: " + _bank);
        System.out.println("Account: " + _account);
        System.out.println("Money: " + _money);
        System.out.println("Date: " + _date);
    }
}
