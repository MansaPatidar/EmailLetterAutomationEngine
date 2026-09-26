package assignment3;

/**
 * Prints stub messages representing future automation behavior.
 */
public class StubNotifier {

  private static final String EMAIL_STUB = "[stub] Sending generated emails to clients.";
  private static final String LETTER_STUB = "[stub] Printing generated letters to send to clients.";

  /**
   * Prints the email stub message indicating that email sending would happen.
   */
  public void printEmailStub() {
    System.out.println(EMAIL_STUB);
  }

  /**
   * Prints the letter stub message indicating that letter printing would happen.
   */
  public void printLetterStub() {
    System.out.println(LETTER_STUB);
  }
}
