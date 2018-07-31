package tran.example.weatherforecast.services;

import java.util.List;

/**
 * Declares methods performing CRUD operations for a specified object.
 * @param <T> A generic object
 */
public interface CRUDService<T> {
    /**
     * @return A list of all the objects of a specified type.
     */
    List<T> listAll();

    /**
     * Gets an object by the specified id.
     * @param id The id of the object to be retrieved.
     * @return The object with the associated id.
     */
    T getById(Long id);

    /**
     * Saves the object if not in the database and updates it if already persisted.
     * @param domainObject The object to be saved or updated.
     * @return An object after being saved or persisted.
     */
    T saveOrUpdate(T domainObject);

    /**
     * Removes an object based on the specified id.
     * @param id The id of the object to be removed.
     */
    void delete(Long id);
}
