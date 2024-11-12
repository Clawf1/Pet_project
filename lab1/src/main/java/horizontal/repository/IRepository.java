package horizontal.repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Generic interface for repositories.
 *
 * @param <T> The type of entity stored in the repository.
 */
public interface IRepository<T> {
    /**
     * Finds an entity by its ID.
     *
     * @param id The ID of the entity to find.
     * @return An Optional containing the found entity, or empty if not found.
     */
    Optional<T> findById(UUID id);
}
