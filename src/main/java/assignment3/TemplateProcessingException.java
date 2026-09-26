package assignment3;

/**
 * Exception thrown when template processing fails.
 */
public class TemplateProcessingException extends RuntimeException {

  /**
   * Constructs a TemplateProcessingException with the specified message.
   * This exception will return specific message based on input combinations by user
   * eg throw new TemplateProcessingException("Missing value for: "); if value does not exist
   * @param message the error message
   */
  public TemplateProcessingException(String message) {

    super("TemplateProcessingException: " + message);
  }
}
