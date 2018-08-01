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
     * Saves the object if not in the database.
     * @param domainObject The object to be saved.
     * @return Returns an object after being saved, null if it already exists.
     */
    T save(T domainObject);

    /**
     * Updates the object.
     * @param domainObject The object to be updated.
     * @return Return an object after being updated.
     */
    T update(T domainObject);

    /**
     * Removes an object based on the specified id.
     * @param id The id of the object to be removed.
     */
    void delete(Long id);
}
