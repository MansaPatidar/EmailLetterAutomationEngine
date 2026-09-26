package assignment3;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TemplateEngineTest {

  private TemplateEngine engine;

  @BeforeEach
  void setUp() {
    this.engine = new TemplateEngine();
  }

  @Test
  void fillTemplate_singlePlaceholder_success() {
    String template = "Hello [[first_name]]!";
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Mansa");

    String result = this.engine.fillTemplate(template, row);

    assertEquals("Hello Mansa!", result);
  }

  @Test
  void fillTemplate_multiplePlaceholders_success() {
    String template = "Dear [[first_name]] [[last_name]], welcome to [[city]].";
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Mansa");
    row.put("last_name", "Singh");
    row.put("city", "Seattle");

    String result = this.engine.fillTemplate(template, row);

    assertEquals("Dear Mansa Singh, welcome to Seattle.", result);
  }

  @Test
  void fillTemplate_missingValue_throwsException() {
    String template = "Hello [[first_name]] [[last_name]]!";
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Mansa");

    assertThrows(TemplateProcessingException.class,
        () -> this.engine.fillTemplate(template, row));
  }

  @Test
  void fillTemplate_noPlaceholders_returnsOriginal() {
    String template = "This is a plain text message.";
    Map<String, String> row = new HashMap<>();

    String result = this.engine.fillTemplate(template, row);

    assertEquals("This is a plain text message.", result);
  }

  @Test
  void fillTemplate_placeholderWithSpaces_success() {
    String template = "Hello [[ first_name ]].";
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Mansa");

    String result = this.engine.fillTemplate(template, row);

    assertEquals("Hello Mansa.", result);
  }

  @Test
  void fillTemplate_duplicatePlaceholders_replacesAll() {
    String template = "[[name]] likes [[name]].";
    Map<String, String> row = new HashMap<>();
    row.put("name", "coffee");

    String result = this.engine.fillTemplate(template, row);

    assertEquals("coffee likes coffee.", result);
  }

  @Test
  void fillTemplate_emptyString_success() {
    String template = "Hello [[first_name]].";
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "");

    String result = this.engine.fillTemplate(template, row);

    assertEquals("Hello .", result);
  }

  @Test
  void fillTemplate_specialCharactersInValue_success() {
    String template = "Email: [[email]]";
    Map<String, String> row = new HashMap<>();
    row.put("email", "user+test@example.com");

    String result = this.engine.fillTemplate(template, row);

    assertEquals("Email: user+test@example.com", result);
  }

  @Test
  void fillTemplate_valueWithBackslash_success() {
    String template = "Path: [[path]]";
    Map<String, String> row = new HashMap<>();
    row.put("path", "C:\\Users\\test");

    String result = this.engine.fillTemplate(template, row);

    assertEquals("Path: C:\\Users\\test", result);
  }

  @Test
  void fillTemplate_placeholderAtStart_success() {
    String template = "[[greeting]] world";
    Map<String, String> row = new HashMap<>();
    row.put("greeting", "Hello");

    String result = this.engine.fillTemplate(template, row);

    assertEquals("Hello world", result);
  }

  @Test
  void fillTemplate_placeholderAtEnd_success() {
    String template = "Hello [[name]]";
    Map<String, String> row = new HashMap<>();
    row.put("name", "world");

    String result = this.engine.fillTemplate(template, row);

    assertEquals("Hello world", result);
  }

  @Test
  void fillTemplate_onlyPlaceholder_success() {
    String template = "[[value]]";
    Map<String, String> row = new HashMap<>();
    row.put("value", "result");

    String result = this.engine.fillTemplate(template, row);

    assertEquals("result", result);
  }

  @Test
  void fillTemplate_missingValue_exceptionMessage() {
    String template = "[[missing]]";
    Map<String, String> row = new HashMap<>();

    TemplateProcessingException exception = assertThrows(
        TemplateProcessingException.class,
        () -> this.engine.fillTemplate(template, row));

    assertTrue(exception.getMessage().contains("missing"));
  }

  @Test
  void fillTemplate_adjacentPlaceholders_success() {
    String template = "[[first]][[second]]";
    Map<String, String> row = new HashMap<>();
    row.put("first", "Hello");
    row.put("second", "World");

    String result = this.engine.fillTemplate(template, row);

    assertEquals("HelloWorld", result);
  }

  @Test
  void fillTemplate_manyPlaceholders_success() {
    String template = "[[a]] [[b]] [[c]] [[d]] [[e]]";
    Map<String, String> row = new HashMap<>();
    row.put("a", "1");
    row.put("b", "2");
    row.put("c", "3");
    row.put("d", "4");
    row.put("e", "5");

    String result = this.engine.fillTemplate(template, row);

    assertEquals("1 2 3 4 5", result);
  }

  @Test
  void fillTemplate_oneOfManyMissing_throwsException() {
    String template = "[[a]] [[b]] [[c]]";
    Map<String, String> row = new HashMap<>();
    row.put("a", "1");
    row.put("b", "2");
    // c is missing

    assertThrows(TemplateProcessingException.class,
        () -> this.engine.fillTemplate(template, row));
  }

  @Test
  void fillTemplate_caseInsensitiveKeyLookup() {
    String template = "[[Name]]";
    Map<String, String> row = new HashMap<>();
    row.put("name", "John");

    // This should fail because keys are case-sensitive
    assertThrows(TemplateProcessingException.class,
        () -> this.engine.fillTemplate(template, row));
  }

  @Test
  void fillTemplate_extraKeysIgnored() {
    String template = "Hello [[first_name]]";
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Alice");
    row.put("last_name", "Wonder");
    row.put("age", "30");

    String result = this.engine.fillTemplate(template, row);

    assertEquals("Hello Alice", result);
  }
}
