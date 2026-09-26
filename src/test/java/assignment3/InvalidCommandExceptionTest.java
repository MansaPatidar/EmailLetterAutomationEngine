package assignment3;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class InvalidCommandExceptionTest {

  @Test
  void constructor_storesMessage() {
    String message = "Test error message";

    InvalidCommandException exception = new InvalidCommandException(message);

    assertEquals("InvalidCommandException: " + message, exception.getMessage());
  }

  @Test
  void constructor_extendsException() {
    InvalidCommandException exception = new InvalidCommandException("Test");

    assertInstanceOf(Exception.class, exception);
  }

  @Test
  void throwAndCatch_throwsCorrectly() {
    String message = "Missing required option";

    try {
      throw new InvalidCommandException(message);
    } catch (InvalidCommandException e) {
      assertEquals("InvalidCommandException: " + message, e.getMessage());
    }
  }

  @Test
  void toString_containsMessage() {
    String message = "Invalid argument";
    InvalidCommandException exception = new InvalidCommandException(message);
    assertNotNull(exception.toString());
    assertTrue(exception.toString().contains("InvalidCommandException: " + message));
  }
}
