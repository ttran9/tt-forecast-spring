package tran.example.weatherforecast.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * A service implementing methods to perform encryption and check stored encrypted strings.
 */
@Service
public class EncryptionServiceImpl implements EncryptionService {

    /**
     * A data member used to encrypt the password with specific type of encoding, in this case
     * BCrypt hashing.
     */
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Initializes the password encoder to use a certain type of encoding, in this case BCrypt
     * hashing will be used.
     * @param passwordEncryptor The encoder to encrypt the password.
     */
    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncryptor) {
        this.passwordEncoder = passwordEncryptor;
    }

    /**
     * Encrypts the provided string using BCrypt hashing.
     * @param input The string to be encrypted.
     * @return A string encrypted using the specified encoder of the class.
     */
    @Override
    public String encryptString(String input) {
        return this.passwordEncoder.encode(input);
    }

    /**
     * Encrypts a plain password and compares this to a passed in encrypted password to determine
     * if they are identical.
     * @param plainPassword The unencrypted password for checking.
     * @param encryptedPassword The encrypted password for checking.
     * @return True if the plain text password encrypted matches the provided encrypted password.
     */
    @Override
    public boolean checkPassword(String plainPassword, String encryptedPassword) {
        return passwordEncoder.matches(plainPassword, encryptedPassword);
    }
}
