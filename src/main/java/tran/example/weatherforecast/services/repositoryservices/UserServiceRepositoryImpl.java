package tran.example.weatherforecast.services.repositoryservices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tran.example.weatherforecast.domain.CustomUser;
import tran.example.weatherforecast.exceptions.NotFoundException;
import tran.example.weatherforecast.repositories.CustomUserRepository;
import tran.example.weatherforecast.services.UserService;
import tran.example.weatherforecast.services.security.EncryptionService;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A service implementing CRUD operations on the CustomUser entities and table.
 */
@Slf4j
@Service
public class UserServiceRepositoryImpl implements UserService {

    /**
     * Allows access to the user table.
     */
    private CustomUserRepository userRepository;
    /**
     * A service used to assist with encrypting a password for storage.
     */
    private EncryptionService encryptionService;

    /**
     * Injects a user repository to be used with this service.
     * @param userRepository The user repository required to perform CRUD operation(s) on Users.
     */
    @Autowired
    public UserServiceRepositoryImpl(CustomUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Initializes the encryption service.
     * @param encryptionService The encryption service to be used to encrypt user passwords.
     */
    @Autowired
    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    /**
     * Returns a list of all the users stored.
     * @return A list of user objects.
     */
    @Override
    public List<CustomUser> listAll() {
        log.debug("Retrieving the users from the database!");
        List<CustomUser> users = new LinkedList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    /**
     * Returns a user object with the provided id.
     * @param id The unique identifier (id) of a user object.
     * @return A user object with the specified id.
     */
    @Override
    public CustomUser getById(Long id) {
        Optional<CustomUser> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()) {
            log.debug("Retrieving a user by ID!");
            return userOptional.get();
        }
        log.debug("CustomUser cannot be found with given ID.");
        throw new NotFoundException("CustomUser cannot be found!");
    }

    /**
     * Updates the user if it already exists or saves the user if the user has not yet been
     * persisted.
     * @param domainObject The user object to be saved or updated.
     * @return The user object after being saved or updated.
     */
    @Override
    public CustomUser saveOrUpdate(CustomUser domainObject) {
        log.debug("Saving or updating a user!");
        if(domainObject.getPassword() != null){
            log.debug("Encrypting password of user: " + domainObject.getUsername());
            domainObject.setEncryptedPassword(encryptionService.encryptString(domainObject.getPassword()));
        }
        return userRepository.save(domainObject);
    }

    /**
     * Removes a user from the database with the specified id.
     * @param id The id of the user to be removed.
     */
    @Override
    public void delete(Long id) {
        log.debug("Deleting a user!");
        userRepository.deleteById(id);
    }

    /**
     * Finds a user based on the provided user name.
     * @param userName The user name of a user.
     * @return The user with the associated user name.
     */
    @Override
    public CustomUser findByUserName(String userName) {
        log.debug("Attempting to find a user by the specified user name!");
        return userRepository.findByUsername(userName);
    }

    /**
     * Checks if the user name is already taken.
     * @param userName The user name to be checked.
     * @return True if the user name already exists in the database, false otherwise.
     */
    @Override
    public Boolean isUserNameTaken(String userName) {
        CustomUser customUser = findByUserName(userName);
        if(customUser != null) {
            return true;
        }
        return false;
    }
}
