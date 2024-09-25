package horizontal.controller;

import java.util.Optional;
import java.util.UUID;

/**
 * Интерфейс IMenu представляет контроллерное меню, которое может быть выполнено и предоставляет методы для чтения ID.
 */
public interface IMenu {
    /**
     * Метод Run запускает выполнение меню.
     */
    void Run();

    /**
     * Метод Next возвращает следующий ввод пользователя.
     *
     * @return Строка с пользовательским вводом.
     */
    String Next();

    /**
     * Метод ReadId считывает и возвращает ID из пользовательского ввода.
     *
     * @return Optional, содержащий UUID, если ввод корректен, или пустой, если ввод некорректен.
     */
    default Optional<UUID> ReadId() {
        System.out.println("Enter ID: ");
        String StringId = Next();
        UUID id;
        try {
            id = UUID.fromString(StringId);
        } catch (IllegalArgumentException e) {
            System.out.println("Wrong ID format.");
            return Optional.empty();
        }
        return Optional.of(id);
    }
}
