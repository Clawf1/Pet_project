package horizontal.service;

import horizontal.model.Bank;
import horizontal.model.accounts.Account;
import horizontal.model.transactions.DepositTransaction;
import horizontal.model.transactions.Transaction;
import horizontal.model.transactions.TransferTransaction;
import horizontal.model.transactions.WithdrawalTransaction;
import horizontal.repository.BankRepository;
import horizontal.repository.TransactionRepository;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для выполнения и отмены транзакций в банковской системе.
 */
@Data
public class TransactionService {
    /**
     * Репозиторий транзакций, используемый для хранения всех проведенных транзакций.
     */
    private final TransactionRepository _transactions = new TransactionRepository();

    private final BankRepository _banks;

    /**
     * Создает новый экземпляр сервиса транзакций.
     *
     * @param banks репозиторий всех существующих банков
     */
    public TransactionService(BankRepository banks) {
        _banks = banks;
    }

    private Bank findBankById(UUID bankId) {
        return _banks.findById(bankId).orElse(null);
    }

    private Account findAccountById(UUID accountId, Bank bank) {
        return bank.getAccounts().findById(accountId).orElse(null);
    }

    /**
     * Выполняет указанную транзакцию.
     *
     * @param transaction транзакция для выполнения
     * @return опциональный объект транзакции, если операция выполнена успешно, иначе пусто
     */
    public Optional<Transaction> DoTransaction(Transaction transaction) {
        var bank = findBankById(transaction.get_bank());
        if (bank == null) return Optional.empty();
        var account = findAccountById(transaction.get_account(), bank);
        if (account == null) return Optional.empty();

        switch (transaction) {
            case DepositTransaction ignored -> {
                DepositTransaction(account, transaction.get_money());
            }
            case WithdrawalTransaction ignored -> {
                if (!WithdrawTransaction(account, transaction.get_money())) return Optional.empty();
            }
            case TransferTransaction transferTransaction -> {
                var to_bank = findBankById(transferTransaction.get_to_bank());
                if (to_bank == null) return Optional.empty();
                var to_account = findAccountById(transferTransaction.get_to_account(), to_bank);
                if (to_account == null) return Optional.empty();
                if (!TransferTransaction(account, transferTransaction, true)) return Optional.empty();
            }
            default -> {
            }
        }
        _transactions.addTransaction(transaction);
        return Optional.of(transaction);
    }

    /**
     * Отменяет указанную транзакцию.
     *
     * @param transaction транзакция для отмены
     * @return опциональный объект транзакции, если операция отменена успешно, иначе пусто
     */
    public Optional<Transaction> UndoTransaction(Transaction transaction) {
        var bank = findBankById(transaction.get_bank());
        if (bank == null) return Optional.empty();
        var account = findAccountById(transaction.get_account(), bank);
        if (account == null) return Optional.empty();

        switch (transaction) {
            case DepositTransaction ignored -> {
                if (!WithdrawTransaction(account, transaction.get_money()))
                    return Optional.empty();
            }
            case WithdrawalTransaction ignored -> DepositTransaction(account, transaction.get_money());
            case TransferTransaction transferTransaction -> {
                if (!TransferTransaction(account, transferTransaction, false))
                    return Optional.empty();
            }
            default -> {
            }
        }
        _transactions.removeTransaction(transaction.get_id());
        return Optional.of(transaction);
    }

    /**
     * Отменяет транзакцию по указанному идентификатору.
     *
     * @param id идентификатор транзакции для отмены
     * @return опциональный объект транзакции, если операция отменена успешно, иначе пусто
     */
    public Optional<Transaction> UndoTransactionById(UUID id) {
        var opt_t = _transactions.findById(id);
        if (opt_t.isEmpty()) return Optional.empty();
        return UndoTransaction(opt_t.get());
    }

    /**
     * Пополняет указанный счет указанной суммой.
     *
     * @param account счет для пополнения
     * @param money   сумма для пополнения
     */
    public void DepositTransaction(Account account, BigDecimal money) {
        account.add(money);
    }

    /**
     * Снимает указанную сумму с указанного счета.
     *
     * @param account счет для снятия
     * @param money   сумма для снятия
     * @return true, если операция выполнена успешно, иначе false
     */
    public boolean WithdrawTransaction(Account account, BigDecimal money) {
        return account.subtract(money);
    }

    /**
     * Выполняет перевод указанной суммы с одного счета на другой.
     *
     * @param account     счет, с которого выполняется перевод
     * @param transaction транзакция перевода
     * @param do_flag     флаг, указывающий на направление выполнения транзакции (true - в прямом порядке, false - в обратном порядке)
     * @return true, если операция выполнена успешно, иначе false
     */
    public boolean TransferTransaction(Account account, TransferTransaction transaction, boolean do_flag) {
        var money = transaction.get_money();
        var to_bank = findBankById(transaction.get_to_bank());
        if (to_bank == null) return false;
        var to_account = findAccountById(transaction.get_to_account(), to_bank);
        if (to_account == null) return false;

        return do_flag ? TransferFromTo(account, to_account, money) :
                TransferFromTo(to_account, account, money);
    }

    private boolean TransferFromTo(Account from, Account to, BigDecimal money) {
        if (!from.subtract(money)) return false;
        to.add(money);
        return true;
    }
}
