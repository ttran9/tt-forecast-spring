package tran.example.weatherforecast.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * A super class to hold fields common amongst all the objects persisted in the database.
 */
@Getter
@Setter
@MappedSuperclass
public class AbstractDomainClass implements DomainObject {

    /**
     * The unique identifier for objects of a table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    /**
     * A field to store when an object was first persisted into the database.
     */
    private Date dateCreated;
    /**
     * A field to store when an object was last modified or persisted into the database.
     */
    private Date lastUpdated;

    /**
     * A method to be invoked before an object is saved/persisted or updated.
     */
    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        lastUpdated = new Date();
        if (dateCreated==null) {
            dateCreated = new Date();
        }
    }
}
