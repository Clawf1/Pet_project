package horizontal;

import horizontal.controller.ConsoleInterface;
import horizontal.model.Bank;
import horizontal.model.Client;
import horizontal.repository.BankRepository;
import horizontal.repository.ClientRepository;

public class Main {
    public static void main(String[] args) {
        var banks = new BankRepository();
        banks.registerBank(Bank.builder("Tinkoff").build());
        banks.registerBank(Bank.builder("Sber").build());

        var clients = new ClientRepository();
        clients.registerClient(Client.builder("Glad Valakas").build());
        clients.registerClient(Client.builder("Denis Petrov")
                .set_passport("7080823415")
                .set_address("Saint Petersburg Lenina st. 12")
                .build());

        var console = ConsoleInterface.builder()
                .setClients(clients)
                .setBanks(banks)
                .build()
                .Initialize();

        console.Run();
    }
}