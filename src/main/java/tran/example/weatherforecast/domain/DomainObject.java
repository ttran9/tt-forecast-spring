package tran.example.weatherforecast.domain;

/**
 * Methods which will be common to all persisted objects in this application's database.
 */
public interface DomainObject {

    /**
     * @return The unique identifier for a persisted object.
     */
    Integer getId();

    /**
     * Sets the unique identifier for an object.
     * @param id The unique ID to be associated with this object.
     */
    void setId(Integer id);
}
