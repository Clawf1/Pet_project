package horizontal.controller;

import horizontal.model.Bank;
import horizontal.model.accounts.Account;
import horizontal.model.accounts.CreditAccount;
import horizontal.model.accounts.DebitAccount;
import horizontal.model.accounts.DepositAccount;
import horizontal.repository.BankRepository;
import horizontal.repository.ClientRepository;
import horizontal.service.CreateAccountService;

import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

/**
 * BankMenu представляет собой меню для управления операциями с банком.
 * Здесь пользователь может найти банк, просмотреть лимиты, зарегистрировать счет, найти счет и показать все счета.
 */
public class BankMenu implements IMenu {

    /** Сервис создания счетов */
    private final CreateAccountService createAccountService = new CreateAccountService();

    private final BankRepository _banks;
    private final ClientRepository _clients;

    private final Scanner _scanner;
    private final PrintStream Out = System.out;

    /**
     * Конструктор BankMenu.
     * @param banks Репозиторий банков.
     * @param clients Репозиторий клиентов.
     * @param scanner Сканер для считывания ввода пользователя.
     */
    public BankMenu(BankRepository banks, ClientRepository clients, Scanner scanner) {
        _banks = banks;
        _clients = clients;
        _scanner = scanner;
    }

    /**
     * Метод для запуска меню управления операциями с банком.
     * Пользователь может найти банк, просмотреть лимиты, зарегистрировать счет, найти счет и показать все счета.
     */
    @Override
    public void Run() {
        Out.println("Welcome to the Bank Menu");
        while (true) {
            Out.println("Bank Menu.");
            Out.println("1. Find Bank");
            Out.println("2. Show Limits");
            Out.println("3. Register Account");
            Out.println("4. Find Account");
            Out.println("5. Show All Accounts");
            Out.println("6. Back to Main Menu");

            switch (_scanner.nextInt()) {
                case 1:
                    FindBankMenu().ifPresentOrElse(Bank::print,
                            () -> Out.println("Bank not found"));
                    break;
                case 2:
                    ShowLimits();
                    break;
                case 3:
                    RegisterAccountMenu().ifPresentOrElse(Account::print,
                            () -> Out.println("Account registration failed, try again."));
                    break;
                case 4:
                    FindAccount().ifPresentOrElse(Account::print,
                            () -> Out.println("Account not found"));
                    break;
                case 5:
                    FindBankMenu().ifPresentOrElse(value -> value.accounts.ShowAllAccounts(),
                            () -> Out.println("No accounts found"));
                    break;
                case 6:
                    return;
            }
        }
    }

    @Override
    public String Next() {
        return _scanner.next();
    }

    /**
     * Метод для поиска банка.
     * Пользователь вводит имя банка, ищется банк с таким именем.
     * @return Возвращает найденный банк в виде объекта Optional.
     */
    public Optional<Bank> FindBankMenu() {
        Out.println("Find bank");
        String name = _scanner.nextLine();
        while (name.isEmpty()) name = _scanner.nextLine();
        String finalName = name;
        return _banks.getAllBanks().stream().filter(b -> b.get_name().equals(finalName)).findAny();
//        var id = ReadId();
//        if (id.isEmpty()) return Optional.empty();
//        return _banks.findById(id.get());
    }

    /**
     * Метод для регистрации счета.
     * Пользователь вводит информацию о клиенте, типе счета и другие необходимые данные.
     * @return Возвращает созданный счет в виде объекта Optional.
     */
    public Optional<Account> RegisterAccountMenu() {
        Out.println("Register account");

        var bank = FindBankMenu();
        if (bank.isEmpty()) return Optional.empty();

        Out.println("Client Id:");
        var clientId = ReadId();
        if (clientId.isEmpty()) return Optional.empty();
        var client = _clients.findById(clientId.get());
        if (client.isEmpty()) return Optional.empty();

        Out.println("Choose Account Type: ");
        Out.println("1. Credit");
        Out.println("2. Debit");
        Out.println("3. Deposit");

        Account account;

        switch (_scanner.nextInt()) {
            case 1:
                account = new CreditAccount(client.get(), bank.get().get_money_limit());
                break;
            case 2:
                account = new DebitAccount(client.get());
                break;
            case 3:
                Out.println("Enter amount of money");
                var money = _scanner.nextBigDecimal();
                Out.println("Enter year in YYYY format");
                String StringDate = _scanner.next() + '-';
                Out.println("Enter month in MM format");
                StringDate += _scanner.next() + '-';
                Out.println("Enter day in DD format");
                StringDate += _scanner.next();
                var date = LocalDate.parse(StringDate);
                account = new DepositAccount(client.get(), money, bank.get().DepositPercent(money), date);
                break;
            default:
                System.out.println("Invalid Account Type");
                return Optional.empty();
        }
        createAccountService.CreateAccount(account, bank.get());
        Out.println("Account created!");
        return Optional.of(account);
    }

    /**
     * Метод для поиска счета.
     * Пользователь вводит информацию о банке и ID счета, затем ищется счет с такими данными.
     * @return Возвращает найденный счет в виде объекта Optional.
     */
    public Optional<Account> FindAccount() {
        Out.println("Find Account");

        var bank = FindBankMenu();
        if (bank.isEmpty()) return Optional.empty();

        Out.println("Account Id:");
        var accountId = ReadId();
        if (accountId.isEmpty()) return Optional.empty();

        return bank.get().accounts.findById(accountId.get());
    }

    /**
     * Метод для отображения лимитов банка.
     * Пользователь вводит имя банка, затем отображаются его лимиты.
     */
    public void ShowLimits() {
        Out.println("Show limits");
        FindBankMenu().ifPresentOrElse(Bank::printLimits,
                () -> Out.println("Bank not found."));
    }
}
