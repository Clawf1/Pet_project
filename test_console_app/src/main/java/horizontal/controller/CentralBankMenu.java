package horizontal.controller;

import horizontal.model.Bank;
import horizontal.service.CentralBankService;

import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

/**
 * CentralBankMenu представляет собой меню для управления операциями с центральным банком.
 * Здесь пользователь может добавлять новые банки, просматривать список всех банков и управлять временем.
 */
public class CentralBankMenu implements IMenu {

    /** Сервис центрального банка */
    private final CentralBankService _centralBank;

    private final Scanner _scanner;
    private final PrintStream Out = System.out;

    /**
     * Конструктор CentralBankMenu.
     * @param centralBank Сервис центрального банка.
     * @param scanner Сканер для считывания ввода пользователя.
     */
    public CentralBankMenu(CentralBankService centralBank, Scanner scanner) {
        _centralBank = centralBank;
        _scanner = scanner;
    }


    /**
     * Метод для запуска меню управления центральным банком.
     * Пользователь может выбрать действия, такие как добавление банка, просмотр списка всех банков и управление временем.
     */
    @Override
    public void Run() {
        Out.println("Welcome to the Central Bank Menu");
        while (true) {
            Out.println("CentralBank. Choose:");
            Out.println("1. Add bank");
            Out.println("2. Show all banks");
            Out.println("3. Skip menu");
            Out.println("4. Back to Main Menu");

            switch (_scanner.nextInt()) {
                case 1:
                    AddBankMenu().ifPresentOrElse(Bank::print,
                            () -> Out.println("Add bank error, try again."));
                    break;
                case 2:
                    ShowAllBanks();
                    break;
                case 3:
                    SkipTimeMenu();
                    break;
                case 4:
                    return;
                default:
                    Out.println("Wrong argument. Try Again.");
            }
        }
    }


    @Override
    public String Next() {
        return _scanner.next();
    }

    /**
     * Метод для добавления нового банка.
     * Пользователь вводит имя нового банка.
     * @return Возвращает созданный банк в виде объекта Optional.
     */
    public Optional<Bank> AddBankMenu() {
        Out.println("Bank Creation");
        Out.println("Enter bank name(necessary):");
        String name = _scanner.next();
        var bankBuilder = Bank.builder(name);
        var bank = bankBuilder.build();
        _centralBank.RegisterBank(bank);
        return Optional.of(bank);
    }

    /**
     * Метод для отображения всех банков.
     */
    public void ShowAllBanks() {
        var banks = _centralBank.get_banks().getAllBanks();
        if (banks.isEmpty()) Out.println("No banks found.");
        for (var bank : banks) {
            bank.print();
        }
    }

    /**
     * Метод для управления временем.
     * Пользователь может выбрать пропустить несколько дней или перейти к определенной дате.
     */
    public void SkipTimeMenu() {
        Out.println("Skip menu");
        while (true) {
            Out.println("Choose:");
            Out.println("1. Skip N days");
            Out.println("2. Skip to date");
            Out.println("3. Back to central bank menu");
            switch (_scanner.nextInt()) {
                case 1:
                    Out.println("Enter N: ");
                    long N = _scanner.nextLong();
                    _centralBank.SkipDays(N);
                    Out.println("Skip days : " + N);
                    break;
                case 2:
                    Out.println("Enter year in format YYYY");
                    String dateString = _scanner.next() + '-';
                    Out.println("Enter month in format MM");
                    dateString += _scanner.next() + '-';
                    Out.println("Enter day in format DD");
                    dateString += _scanner.next();

                    LocalDate date = LocalDate.parse(dateString);
                    _centralBank.SkipTimeTo(date);
                    Out.println("Skip time to :" + date);
                    break;
                case 3:
                    return;
            }
        }
    }
}
