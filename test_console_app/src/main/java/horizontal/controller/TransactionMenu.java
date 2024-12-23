package horizontal.controller;

import horizontal.model.transactions.DepositTransaction;
import horizontal.model.transactions.Transaction;
import horizontal.model.transactions.TransferTransaction;
import horizontal.model.transactions.WithdrawalTransaction;
import horizontal.repository.BankRepository;
import horizontal.service.TransactionService;

import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

/**
 * TransactionMenu представляет собой меню для управления операциями с транзакциями.
 * Позволяет пользователю создавать транзакции, отменять их и находить по ID.
 */
public class TransactionMenu implements IMenu {
    /** Сервис для управления транзакциями */
    private final TransactionService transactionService;

    private final Scanner _scanner;
    private final PrintStream Out = System.out;

    /**
     * Конструктор TransactionMenu.
     * @param bankRepository Репозиторий банков, необходим для инициализации TransactionService.
     * @param scanner Сканер для считывания ввода пользователя.
     */
    public TransactionMenu(BankRepository bankRepository, Scanner scanner) {
        transactionService = new TransactionService(bankRepository);
        _scanner = scanner;
    }

    /**
     * Метод для запуска меню управления операциями с транзакциями.
     * Позволяет пользователю создавать транзакции, отменять их и находить по ID.
     */
    @Override
    public void Run() {
        Out.println("Welcome to the BankSystem");

        while (true) {
            Out.println("Choose:");
            Out.println("1. Create Transaction");
            Out.println("2. Undo Transaction");
            Out.println("3. Find Transaction");
            Out.println("4. Back to Main Menu");

            switch (_scanner.nextInt()) {
                case 1:
                    CreateTransactionMenu().ifPresentOrElse(Transaction::print,
                            () -> Out.println("Transaction error, try again"));;
                    break;
                case 2:
                    UndoMenu().ifPresentOrElse(Transaction::print,
                            () -> Out.println("Undo Transaction error, try again"));;
                    break;
                case 3:
                    FindTransactionMenu().ifPresentOrElse(Transaction::print,
                            () -> Out.println("Transaction not found"));
                    break;
                case 4:
                    return;
            }
        }
    }

    @Override
    public String Next() {
        return _scanner.next();
    }

    /**
     * Метод для создания транзакции.
     * Пользователь выбирает тип транзакции, вводит необходимые данные и создает транзакцию.
     * @return Возвращает созданную транзакцию в виде объекта Optional.
     */
    public Optional<Transaction> CreateTransactionMenu() {
        Out.println("Create Transaction Menu");

        Out.println("Bank id");
        var bankId = ReadId();
        if (bankId.isEmpty()) return Optional.empty();

        Out.println("Account id");
        var accountId = ReadId();
        if (accountId.isEmpty()) return Optional.empty();

        Out.println("Enter amount of money");
        var money = _scanner.nextBigDecimal();

        Out.println("Choose Transaction Type:");
        Out.println("1. Deposit");
        Out.println("2. Withdraw");
        Out.println("3. Transfer");

        Transaction transaction;

        switch (_scanner.nextInt()) {
            case 1:
                transaction = new DepositTransaction(bankId.get(),
                        accountId.get(),
                        money);
                break;
            case 2:
                transaction = new WithdrawalTransaction(bankId.get(),
                        accountId.get(),
                        money);
                break;
            case 3:
                Out.println("ToBank id");
                var toBankId = ReadId();
                if (toBankId.isEmpty()) return Optional.empty();

                Out.println("ToAccount id");
                var toAccountId = ReadId();
                if (toAccountId.isEmpty()) return Optional.empty();

                transaction = new TransferTransaction(bankId.get(),
                        accountId.get(),
                        toBankId.get(),
                        toAccountId.get(),
                        money);
                break;
            default:
                Out.println("Wrong Transaction Type");
                return Optional.empty();
        }
        return transactionService.DoTransaction(transaction);
    }

    /**
     * Метод для отмены транзакции.
     * Пользователь вводит ID транзакции и отменяет её.
     * @return Возвращает отмененную транзакцию в виде объекта Optional.
     */
    public Optional<Transaction> UndoMenu() {
        Out.println("Undo Menu");
        Out.println("Undo transaction Id: ");
        var id = ReadId();
        if (id.isEmpty()) {
            System.out.println("Please enter a transaction Id");
            return Optional.empty();
        }
        return transactionService.UndoTransactionById(id.get());
    }

    /**
     * Метод для поиска транзакции по ID.
     * Пользователь вводит ID транзакции и находит её.
     * @return Возвращает найденную транзакцию в виде объекта Optional.
     */
    public Optional<Transaction> FindTransactionMenu() {
        Out.println("Find bank");
        var id = ReadId();
        if (id.isEmpty()) return Optional.empty();
        return transactionService.get_transactions().findById(id.get());
    }
}
