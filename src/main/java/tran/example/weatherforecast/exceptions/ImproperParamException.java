package tran.example.weatherforecast.exceptions;

/**
 * A class to handle a specific an exception where a passed in a parameter such as an address is
 * invalid (such as it being blank) or not formatted in the desired way.
 */
public class ImproperParamException extends RuntimeException {
    /**
     * Constructs a new improper exception with a message.
     */
    public ImproperParamException(String message) {
        super(message);
    }
}
