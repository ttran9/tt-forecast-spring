package tran.example.weatherforecast.exceptions;

/**
 * A class to handle a specific type of exception which will be thrown by services used in this
 * application and caught by the proper exception handler helper methods.
 */
public class NotFoundException extends RuntimeException  {
    /**
     * Constructs a new notfound exception with a message.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
