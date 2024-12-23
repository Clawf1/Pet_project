package horizontal.controller;

import horizontal.repository.BankRepository;
import horizontal.repository.ClientRepository;
import horizontal.repository.TransactionRepository;
import horizontal.service.CentralBankService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.PrintStream;
import java.util.Scanner;

@AllArgsConstructor
@Data
@Builder(setterPrefix = "set")
public class ConsoleInterface {
    /** Репозитории*/
    @Builder.Default
    private final ClientRepository clients = new ClientRepository();
    @Builder.Default
    private final BankRepository banks = new BankRepository();
    private final TransactionRepository transactions = new TransactionRepository();

    /** Меню для работы с сущностями*/
    private ClientMenu clientMenu;
    private BankMenu bankMenu;
    private TransactionMenu transactionMenu;
    private CentralBankMenu centralBankMenu;

    /** Сканер для считывания ввода пользователя */
    private final Scanner _scanner = new Scanner(System.in);

    /** Поток вывода */
    private final PrintStream Out = System.out;

    /**
     * Метод для инициализации основных компонентов приложения.
     * Создает экземпляры меню и связывает их с соответствующими репозиториями и сервисами.
     * @return Возвращает текущий экземпляр ConsoleInterface.
     */
    public ConsoleInterface Initialize() {
        CentralBankService centralBankService = new CentralBankService(banks);
        clientMenu = new ClientMenu(clients, _scanner);
        bankMenu = new BankMenu(banks, clients, _scanner);
        transactionMenu = new TransactionMenu(banks, _scanner);
        centralBankMenu = new CentralBankMenu(centralBankService, _scanner);
        return this;
    }

    /**
     * Метод для запуска основного цикла взаимодействия с пользователем.
     * Выводит приветственное сообщение и предоставляет пользователю выбор основных меню приложения.
     */
    public void Run() {
        Out.println("Welcome to the BankSystem");

        while (true) {
            Out.println("Choose:");
            Out.println("1. Client menu");
            Out.println("2. Bank menu");
            Out.println("3. Transaction menu");
            Out.println("4. CentralBank menu");
            Out.println("5. Exit");

            switch (_scanner.nextInt()) {
                case 1:
                    clientMenu.Run();
                    break;
                case 2:
                    bankMenu.Run();
                    break;
                case 3:
                    transactionMenu.Run();
                    break;
                case 4:
                    centralBankMenu.Run();
                    break;
                case 5:
                    return;
                default:
                    Out.println("Wrong argument. Try Again.");
            }
        }
    }
}


