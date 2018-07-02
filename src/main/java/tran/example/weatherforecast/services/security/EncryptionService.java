package tran.example.weatherforecast.services.security;

/**
 * Interface to declare methods used for encryption, in this application for encrypting plaintext
 * strings to be stored.
 */
public interface EncryptionService {
    /**
     * Encrypts a plaintext string.
     * @param input The string to be encrypted.
     * @return An encrypted string.
     */
    String encryptString(String input);

    /**
     * Encrypts the plaintext password and checks if it matches the provided encrypted password.
     * @param plainPassword The unencrypted password for checking.
     * @param encryptedPassword The encrypted password for checking.
     * @return True if the plain text password encrypted matches the provided encrypted password,
     * false otherwise.
     */
    boolean checkPassword(String plainPassword, String encryptedPassword);
}
