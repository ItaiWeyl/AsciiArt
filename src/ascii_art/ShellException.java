package ascii_art;

/**
 * Custom exception for handling errors related to shell commands in the ASCII art application.
 */
public class ShellException extends Exception {

    /**
     * Constructs a new ShellException with the specified detail message.
     *
     * @param message the detail message.
     */
    public ShellException(String message) {
        super(message);
    }

    /**
     * Constructs a new ShellException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause the cause of the exception.
     */
    public ShellException(String message, Throwable cause) {
        super(message, cause);
    }
}
