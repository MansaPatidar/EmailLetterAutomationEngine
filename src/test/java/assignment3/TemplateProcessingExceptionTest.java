package assignment3;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TemplateProcessingExceptionTest {

  @Test
  void constructor_storesMessage() {
    String message = "Template error";

    TemplateProcessingException exception = new TemplateProcessingException(message);

    assertEquals("TemplateProcessingException: " + message, exception.getMessage());
  }

  @Test
  void constructor_extendsRuntimeException() {
    TemplateProcessingException exception = new TemplateProcessingException("Test");

    assertInstanceOf(RuntimeException.class, exception);
  }

  @Test
  void throwAndCatch_throwsCorrectly() {
    String message = "Missing placeholder value";

    try {
      throw new TemplateProcessingException(message);
    } catch (TemplateProcessingException e) {
      assertEquals("TemplateProcessingException: " + message, e.getMessage());
    }
  }

  @Test
  void toString_containsMessage() {
    String message = "Template processing failed";
    TemplateProcessingException exception = new TemplateProcessingException(message);
    assertNotNull(exception.toString());
    assertTrue(exception.toString().contains("TemplateProcessingException: " + message));
  }

  @Test
  void canBeThrown_asRuntimeException() {
    TemplateProcessingException exception = new TemplateProcessingException("Error");

    assertThrows(RuntimeException.class, () -> {
      throw exception;
    });
  }
}
