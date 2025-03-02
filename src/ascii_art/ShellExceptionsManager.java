package ascii_art;

/**
 * A utility class to handle and throw specific ShellException types
 * for various command-related errors in the ASCII art application.
 */
public class ShellExceptionsManager {

    /**
     * Throws an exception based on the operation (add or remove) with a specific message.
     *
     * @param isAdd boolean indicating whether the operation is "add" (true) or "remove" (false).
     * @throws ShellException if the format is incorrect for the operation.
     */
    public void throwAddRemoveCharException(boolean isAdd) throws ShellException {
        if (isAdd) {
            throwAddCharException();
        } else {
            throwRemoveCharException();
        }
    }

    /*
     * Throws an exception for an incorrect "add" character operation.
     *
     * @throws ShellException indicating incorrect format while adding a character.
     */
    private void throwAddCharException() throws ShellException {
        System.out.println("Did not add due to incorrect format.");
        throw new ShellException("Incorrect format while adding character.");
    }

    /*
     * Throws an exception for an incorrect "remove" character operation.
     *
     * @throws ShellException indicating incorrect format while removing a character.
     */
    private void throwRemoveCharException() throws ShellException {
        System.out.println("Did not remove due to incorrect format.");
        throw new ShellException("Incorrect format while removing character.");
    }

    /**
     * Throws an exception for an out-of-bound resolution change.
     *
     * @throws ShellException indicating that the resolution exceeded boundaries.
     */
    public void throwResolutionRangeException() throws ShellException {
        System.out.println("Did not change resolution due to exceeding boundaries.");
        throw new ShellException("Resolution exceeded boundaries.");
    }

    /**
     * Throws an exception for an incorrect resolution format.
     *
     * @throws ShellException indicating an incorrect format for resolution.
     */
    public void throwResolutionFormatException() throws ShellException {
        System.out.println("Did not change resolution due to incorrect format.");
        throw new ShellException("Incorrect format for resolution.");
    }

    /**
     * Throws an exception for an incorrect rounding method format.
     *
     * @throws ShellException indicating an incorrect format for rounding method.
     */
    public void throwRoundFormatException() throws ShellException {
        System.out.println("Did not change rounding method due to incorrect format.");
        throw new ShellException("Incorrect format for rounding method.");
    }

    /**
     * Throws an exception for an incorrect output method format.
     *
     * @throws ShellException indicating an incorrect format for output method.
     */
    public void throwOutputFormatException() throws ShellException {
        System.out.println("Did not change output method due to incorrect format.");
        throw new ShellException("Incorrect format for output method.");
    }

    /**
     * Throws an exception for a charset that is too small to execute an operation.
     *
     * @throws ShellException indicating that the charset size is too small.
     */
    public void throwCharSetTooSmallException() throws ShellException {
        System.out.println("Did not execute. Charset is too small.");
        throw new ShellException("Charset size is too small.");
    }

    /**
     * Throws an exception for an illegal or unrecognized command.
     *
     * @throws ShellException indicating that an illegal command was used.
     */
    public void throwIllegalCommandException() throws ShellException {
        System.out.println("Did not execute due to incorrect command.");
        throw new ShellException("Illegal command.");
    }
}
