package horizontal.controller;

import horizontal.model.Client;
import horizontal.repository.ClientRepository;
import lombok.Data;

import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

/**
 * ClientMenu представляет собой меню для управления операциями с клиентами банка.
 * Здесь пользователь может искать клиентов, добавлять новых клиентов и просматривать список всех клиентов.
 */
@Data
public class ClientMenu {

    private final Scanner _scanner;
    private final PrintStream Out = System.out;
    private final ClientRepository _clients;

    /**
     * Конструктор ClientMenu.
     * @param clients Репозиторий клиентов.
     * @param scanner Сканер для считывания ввода пользователя.
     */
    public ClientMenu(ClientRepository clients, Scanner scanner) {
        _scanner = scanner;
        this._clients = clients;
    }

    /**
     * Метод для запуска меню управления клиентами.
     * Пользователь может выбрать действия, такие как поиск клиента, добавление нового клиента и просмотр списка всех клиентов.
     */
    public void Run() {
        Out.println("Welcome to the Client Menu");
        while (true) {
            Out.println("1. Find client");
            Out.println("2. Add client");
            Out.println("3. Show all clients");
            Out.println("4. Back to menu");
            
            switch (_scanner.nextInt()) {
                case 1:
                    FindClientMenu().ifPresentOrElse(Client::print,
                            () -> Out.println("Client not found."));
                    break;
                case 2:
                    AddClientMenu();
                    break;
                case 3:
                    ShowAllClients();
                    break;
                case 4:
                    return;
                default:
                    Out.println("Wrong argument. Try Again.");
            }
        }
    }

    /**
     * Метод для добавления нового клиента.
     * Пользователь вводит имя, паспортные данные и адрес нового клиента.
     */
    public void AddClientMenu() {
        Out.println("Client Creation");
        Out.println("Enter name(necessary):");
        _scanner.nextLine();
        String name = _scanner.nextLine();
        while (name.isEmpty()) {
            Out.println("Name cannot be empty.");
            name = _scanner.nextLine();
        }
        var clientBuilder = Client.builder(name);

        Out.println("Enter passport(optional):");
        String passport = _scanner.nextLine();
        if (!passport.isEmpty())
            clientBuilder.set_passport(passport);
        else Out.println("skipping passport");

        Out.println("Enter address(optional):");
        String address = _scanner.nextLine();
        if (!address.isEmpty())
            clientBuilder.set_address(address);
        else Out.println("skipping address");
        var client = clientBuilder.build();
        _clients.registerClient(client);
        client.print();
    }

    /**
     * Метод для поиска клиента по его идентификатору.
     * @return Возвращает найденного клиента в виде объекта Optional.
     */
    public Optional<Client> FindClientMenu() {
        Out.println("Find client");
        Out.println("Enter ID: ");

        String clientId = _scanner.next();
        UUID id;
        try {
            id = UUID.fromString(clientId);
        } catch (IllegalArgumentException e) {
            Out.println("Wrong ID format.");
            return Optional.empty();
        }
        return _clients.findById(id);
    }

    /**
     * Метод для отображения списка всех клиентов.
     */
    public void ShowAllClients() {
        var clientsList = _clients.getAllClients();
        if (clientsList.isEmpty()) Out.println("No clients found.");
        for (var client : clientsList) {
            client.print();
        }
    }
}
