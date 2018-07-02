package tran.example.weatherforecast.domain.security;

import lombok.Getter;
import lombok.Setter;
import tran.example.weatherforecast.domain.AbstractDomainClass;
import tran.example.weatherforecast.domain.User;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * A database object to hold information about the Role of a user.
 */
@Entity
@Getter
@Setter
public class Role extends AbstractDomainClass {

    /**
     * The name of the role.
     */
    private String role;

    /**
     * A list holding the users that are associated with this specific role.
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();
}
