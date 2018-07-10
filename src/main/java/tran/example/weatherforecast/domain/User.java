package tran.example.weatherforecast.domain;

import lombok.Getter;
import lombok.Setter;
import tran.example.weatherforecast.domain.security.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A persisted object holding fields for an authenticated User.
 */
@Entity
@Getter
@Setter
public class User extends AbstractDomainClass {
    /**
     * The user name.
     */
    private String username;
    @Transient // don't persist to the db (don't want to store plain text pw)
    /**
     * The plain text password for a user which will be encrypted and not persisted into the
     * database.
     */
    private String password;
    /**
     * The encrypted password of a user (BCrypt hashing is used for this application).
     */
    private String encryptedPassword;
    /**
     * The status if an account is enabled or disabled, defaults to true for this account as no
     * accounts will be disabled upon creation.
     */
    private Boolean enabled = true;
    /**
     * A list of the roles associated with this user.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    /**
     * A list of the searches made by the user.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Search> searches = new ArrayList<>();

    /**
     * Adds an association between a user and a role if not present.
     * @param role The role to be added to this user if it is not already assigned to the current
     *            user.
     */
    public void addRole(Role role){
        if(!this.roles.contains(role)){
            this.roles.add(role);
        }

        if(!role.getUsers().contains(this)){
            role.getUsers().add(this);
        }
    }

    /**
     * Removes the association between a user and a role.
     * @param role The role to be removed.
     */
    public void removeRole(Role role){
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    /**
     * Two searches are not likely made by the user at the same time so there is on need to check
     * if the search already exists before appending it to the list of searches.
     * @param search The search to be added.
     */
    public void addSearch(Search search) {
        // associate the search to the user.
        search.setUser(this);
        // add the search to the list of searches.
        this.searches.add(search);
    }

}
