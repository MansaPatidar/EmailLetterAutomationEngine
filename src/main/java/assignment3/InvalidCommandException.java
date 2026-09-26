package assignment3;

/**
 * Exception thrown when command line arguments are missing or invalid.
 */
public class InvalidCommandException extends Exception {

  /**
   * Constructs an InvalidCommandException with the specified message.
   * This exception will return specific message based on input combinations by user
   * eg throw new InvalidCommandException("Missing required option: --output-dir\n") if user does not specify output directory
   * @param message the error message
   */
  public InvalidCommandException(String message) {
    super("InvalidCommandException: " + message);
  }
}
