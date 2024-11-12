package horizontal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import horizontal.model.accounts.CreditAccount;
import horizontal.model.accounts.DebitAccount;
import horizontal.model.accounts.DepositAccount;
import horizontal.repository.AccountRepository;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents a bank in the system.
 */
@Data
@Builder(setterPrefix = "set")
public class Bank {
    @JsonIgnore
    private String _name;

    @Builder.Default
    private UUID _id = UUID.randomUUID();
    @Builder.Default
    private BigDecimal _money_limit = BigDecimal.valueOf(15000),
            _deposit_percent = BigDecimal.valueOf(0.00003),
            _debit_percent = BigDecimal.valueOf(0.00003),
            _credit_percent = BigDecimal.valueOf(0.00003);
    @Builder.Default
    public AccountRepository accounts = new AccountRepository();

    /**
     * Creates a new bank builder with the specified name.
     * @param name The name of the bank.
     * @return A new bank builder.
     */ 
    public static BankBuilder builder(String name) {
        return new BankBuilder().set_name(name);
    }

    public BigDecimal DepositPercent(BigDecimal money) {
        return BigDecimal.valueOf(0.00003).multiply(money);
    }

    /**
     * Simulates the passage of a day, adjusting cashback for accounts.
     */
    public void SkipDay() {
        for (var account : accounts.getAllAccounts()) {
            if (account instanceof DebitAccount) account.ChangeCashback(_debit_percent);
            else if (account instanceof CreditAccount) account.ChangeCashback(_credit_percent);
            else if (account instanceof DepositAccount) account.ChangeCashback(_deposit_percent);
        }
    }

    /**
     * Accrues cashback for all accounts.
     */
    public void AccrueCashback() {
        for (var account : accounts.getAllAccounts()) {
            account.accrue();
        }
    }

    /**
     * Prints the bank's ID and name.
     */
    public void print() {
        System.out.println("ID: " + _id);
        System.out.println("Name: " + _name);
    }

    /**
     * Prints the bank's limits and percentages.
     */
    public void printLimits() {
        System.out.println("Money limit: " + _money_limit);
        System.out.println("Deposit percent: " + _deposit_percent);
        System.out.println("Debit percent: " + _debit_percent);
        System.out.println("Credit percent: " + _credit_percent);
    }
}
