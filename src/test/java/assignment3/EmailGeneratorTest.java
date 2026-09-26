package assignment3;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmailGeneratorTest {

  private TemplateEngine engine;
  private EmailGenerator generator;

  @BeforeEach
  void setUp() {
    this.engine = new TemplateEngine();
    this.generator = new EmailGenerator(engine);
  }

  @Test
  void generate_createsEmailFileWithCorrectContent() throws IOException {
    // Prepare row data
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Mansa");
    row.put("last_name", "Patidar");

    // Template
    String template = "Hello [[first_name]] [[last_name]]!";

    // Temporary output directory
    Path outDir = Files.createTempDirectory("email-test");

    // Run generator
    generator.generate(row, outDir, template);

    // Expected file
    Path expectedFile = outDir.resolve("email-Mansa-Patidar.txt");

    assertTrue(Files.exists(expectedFile));

    // Verify content
    String content = Files.readString(expectedFile, StandardCharsets.UTF_8);
    assertEquals("Hello Mansa Patidar!", content);
  }

  @Test
  void generate_sanitizesWhitespaceInNames() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Mansa Dev");
    row.put("last_name", "Patidar Singh");

    String template = "Hi [[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("email-test");

    generator.generate(row, outDir, template);

    // Whitespace replaced with underscores
    Path expectedFile = outDir.resolve("email-Mansa_Dev-Patidar_Singh.txt");

    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_handlesNullNames() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", null);
    row.put("last_name", null);

    String template = "Hello [[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("email-test");

    // This will throw TemplateProcessingException because placeholder value is null
    assertThrows(TemplateProcessingException.class,
        () -> generator.generate(row, outDir, template));
  }

  @Test
  void generate_multipleRows_createsMultipleFiles() throws IOException {
    Path outDir = Files.createTempDirectory("email-test");

    Map<String, String> row1 = new HashMap<>();
    row1.put("first_name", "Alice");
    row1.put("last_name", "Smith");
    String template = "Hello [[first_name]] [[last_name]]";

    generator.generate(row1, outDir, template);

    Map<String, String> row2 = new HashMap<>();
    row2.put("first_name", "Bob");
    row2.put("last_name", "Jones");

    generator.generate(row2, outDir, template);

    assertTrue(Files.exists(outDir.resolve("email-Alice-Smith.txt")));
    assertTrue(Files.exists(outDir.resolve("email-Bob-Jones.txt")));
  }

  @Test
  void generate_namesWithNumbers_sanitized() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "John2");
    row.put("last_name", "Doe3");

    String template = "Hello [[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("email-test");

    generator.generate(row, outDir, template);

    Path expectedFile = outDir.resolve("email-John2-Doe3.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_namesWithOnlySpaces_becomeUnderscores() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "A B C");
    row.put("last_name", "X Y Z");

    String template = "Hello [[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("email-test");

    generator.generate(row, outDir, template);

    Path expectedFile = outDir.resolve("email-A_B_C-X_Y_Z.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_emptyNameValues_createsFileWithEmptyNames() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "");
    row.put("last_name", "");

    String template = "Hello [[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("email-test");

    generator.generate(row, outDir, template);

    // Empty strings stay empty, not replaced with "unknown"
    Path expectedFile = outDir.resolve("email--.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_missingFirstNameKey_throwsException() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("last_name", "Smith");

    String template = "Hello [[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("email-test");

    assertThrows(TemplateProcessingException.class,
        () -> generator.generate(row, outDir, template));
  }

  @Test
  void generate_specialCharactersInNames_sanitized() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Jo@hn");
    row.put("last_name", "Do$e");

    String template = "Title";
    Path outDir = Files.createTempDirectory("email-test");

    generator.generate(row, outDir, template);

    // Non-alphanumeric characters should remain, only spaces become underscores
    Path expectedFile = outDir.resolve("email-Jo@hn-Do$e.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_fileCreatesOutputDirectory_ifNotExists() throws IOException {
    Path outDir = Files.createTempDirectory("email-test-parent");
    Path nonExistentDir = outDir.resolve("subdir");

    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Test");
    row.put("last_name", "User");

    String template = "Content";

    generator.generate(row, nonExistentDir, template);

    assertTrue(Files.exists(nonExistentDir.resolve("email-Test-User.txt")));
  }

  @Test
  void generate_nullFirstName_sanitizesAsUnknown() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", null);
    row.put("last_name", "Patidar");

    String template = "Hello [[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("email-test");

    // sanitize() returns "unknown" for null values, causing template error
    assertThrows(TemplateProcessingException.class,
        () -> generator.generate(row, outDir, template));
  }

  @Test
  void generate_nullLastName_sanitizesAsUnknown() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "John");
    row.put("last_name", null);

    String template = "Hello [[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("email-test");

    assertThrows(TemplateProcessingException.class,
        () -> generator.generate(row, outDir, template));
  }

  @Test
  void generate_bothNamesNull_createsFileWithUnknownNames() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", null);
    row.put("last_name", null);

    String template = "Content";
    Path outDir = Files.createTempDirectory("email-test");

    generator.generate(row, outDir, template);

    // Both null become "unknown"
    Path expectedFile = outDir.resolve("email-unknown-unknown.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_singleSpace_becomesUnderscore() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "A B");
    row.put("last_name", "X Y");

    String template = "Hi";
    Path outDir = Files.createTempDirectory("email-test");

    generator.generate(row, outDir, template);

    Path expectedFile = outDir.resolve("email-A_B-X_Y.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_tabAndNewline_replacedWithUnderscore() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "A\tB");
    row.put("last_name", "X\nY");

    String template = "Content";
    Path outDir = Files.createTempDirectory("email-test");

    generator.generate(row, outDir, template);

    Path expectedFile = outDir.resolve("email-A_B-X_Y.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_leadingAndTrailingSpaces_convertedToUnderscore() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "  Alice  ");
    row.put("last_name", "  Smith  ");

    String template = "Hello";
    Path outDir = Files.createTempDirectory("email-test");

    generator.generate(row, outDir, template);

    Path expectedFile = outDir.resolve("email-_Alice_-_Smith_.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_writeTemplate_writesUtf8ContentCorrectly() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "José");
    row.put("last_name", "García");

    String template = "Hello [[first_name]] [[last_name]]! Café ☕";
    Path outDir = Files.createTempDirectory("email-test");

    generator.generate(row, outDir, template);

    Path file = outDir.resolve("email-José-García.txt");
    String content = Files.readString(file, StandardCharsets.UTF_8);
    assertEquals("Hello José García! Café ☕", content);
  }

  @Test
  void generate_longNames_allIncludedInFilename() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Christopher");
    row.put("last_name", "Montgomery");

    String template = "Hi";
    Path outDir = Files.createTempDirectory("email-test");

    generator.generate(row, outDir, template);

    Path expectedFile = outDir.resolve("email-Christopher-Montgomery.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_numericNames_preserved() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "123");
    row.put("last_name", "456");

    String template = "Code";
    Path outDir = Files.createTempDirectory("email-test");

    generator.generate(row, outDir, template);

    Path expectedFile = outDir.resolve("email-123-456.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_missingFirstNameKey_causesNullPointerInTemplate() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("last_name", "Smith");

    String template = "Hello";
    Path outDir = Files.createTempDirectory("email-test");

    generator.generate(row, outDir, template);

    // first_name is null, sanitizes to "unknown"
    Path expectedFile = outDir.resolve("email-unknown-Smith.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_onlyFirstName_lastNameNull() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "John");

    String template = "Hi";
    Path outDir = Files.createTempDirectory("email-test");

    generator.generate(row, outDir, template);

    Path expectedFile = outDir.resolve("email-John-unknown.txt");
    assertTrue(Files.exists(expectedFile));
  }
}