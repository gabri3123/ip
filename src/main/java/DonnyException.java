/**
 * Represents an exception specific to the Donny application.
 */
public class DonnyException extends Exception {

    /**
     * Creates a DonnyException with the given error message.
     *
     * @param message error message
     */
    public DonnyException(String message) {
        super(message);
    }
}