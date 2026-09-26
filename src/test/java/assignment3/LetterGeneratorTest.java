package assignment3;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LetterGeneratorTest {

  private TemplateEngine engine;
  private LetterGenerator generator;

  @BeforeEach
  void setUp() {
    this.engine = new TemplateEngine();
    this.generator = new LetterGenerator(engine);
  }

  @Test
  void generate_createsLetterFileWithCorrectContent() throws IOException {
    // Prepare row data
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Mansa");
    row.put("last_name", "Patidar");

    // Template
    String template = "Dear [[first_name]] [[last_name]], welcome.";

    // Temporary output directory
    Path outDir = Files.createTempDirectory("letter-test");

    // Run generator
    generator.generate(row, outDir, template);

    // Expected file
    Path expectedFile = outDir.resolve("letter-Mansa-Patidar.txt");

    assertTrue(Files.exists(expectedFile));

    // Verify content
    String content = Files.readString(expectedFile, StandardCharsets.UTF_8);
    assertEquals("Dear Mansa Patidar, welcome.", content);
  }

  @Test
  void generate_sanitizesWhitespaceInNames() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Mansa Dev");
    row.put("last_name", "Patidar Singh");

    String template = "Hello [[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("letter-test");

    generator.generate(row, outDir, template);

    // Whitespace replaced with underscores
    Path expectedFile = outDir.resolve("letter-Mansa_Dev-Patidar_Singh.txt");

    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_missingValue_throwsException() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Mansa");
    row.put("last_name", null); // Missing value

    String template = "Hello [[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("letter-test");

    assertThrows(TemplateProcessingException.class,
        () -> generator.generate(row, outDir, template));
  }
  @Test
  void generate_nullNameProducesEmptySanitizedValue() throws Exception {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", null);
    row.put("last_name", "Patidar");

    String template = "Hello [[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("letter-test");

    assertThrows(TemplateProcessingException.class,
        () -> generator.generate(row, outDir, template));
  }

  @Test
  void generate_multipleRows_createsMultipleFiles() throws IOException {
    Path outDir = Files.createTempDirectory("letter-test");

    Map<String, String> row1 = new HashMap<>();
    row1.put("first_name", "Charlie");
    row1.put("last_name", "Brown");
    String template = "Dear [[first_name]] [[last_name]]";

    generator.generate(row1, outDir, template);

    Map<String, String> row2 = new HashMap<>();
    row2.put("first_name", "Lucy");
    row2.put("last_name", "Gray");

    generator.generate(row2, outDir, template);

    assertTrue(Files.exists(outDir.resolve("letter-Charlie-Brown.txt")));
    assertTrue(Files.exists(outDir.resolve("letter-Lucy-Gray.txt")));
  }

  @Test
  void generate_namesWithNumbers_sanitized() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Agent7");
    row.put("last_name", "Smith88");

    String template = "Hello [[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("letter-test");

    generator.generate(row, outDir, template);

    Path expectedFile = outDir.resolve("letter-Agent7-Smith88.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_namesWithMultipleSpaces_collapsedToSingleUnderscore() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Mary   Anne");
    row.put("last_name", "O   Brien");

    String template = "Dear [[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("letter-test");

    generator.generate(row, outDir, template);

    // Multiple spaces are collapsed to single underscore by replaceAll("\\s+", "_")
    Path expectedFile = outDir.resolve("letter-Mary_Anne-O_Brien.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_emptyNameValues_createsFileWithEmptyNames() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "");
    row.put("last_name", "");

    String template = "Letter content";
    Path outDir = Files.createTempDirectory("letter-test");

    generator.generate(row, outDir, template);

    // Empty strings stay empty, not replaced with "unknown"
    Path expectedFile = outDir.resolve("letter--.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_fileContentCorrect() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "James");
    row.put("last_name", "Bond");

    String template = "Dear [[first_name]] [[last_name]],\nYour mission awaits.";
    Path outDir = Files.createTempDirectory("letter-test");

    generator.generate(row, outDir, template);

    Path file = outDir.resolve("letter-James-Bond.txt");
    String content = Files.readString(file);

    assertEquals("Dear James Bond,\nYour mission awaits.", content);
  }

  @Test
  void generate_createsDirectoryIfMissing() throws IOException {
    Path outDir = Files.createTempDirectory("letter-test-parent");
    Path nonExistentDir = outDir.resolve("letters");

    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Diana");
    row.put("last_name", "Prince");

    String template = "Greetings [[first_name]]";

    generator.generate(row, nonExistentDir, template);

    assertTrue(Files.exists(nonExistentDir.resolve("letter-Diana-Prince.txt")));
  }

  @Test
  void generate_missingLastNameKey_throwsException() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Test");

    String template = "[[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("letter-test");

    assertThrows(TemplateProcessingException.class,
        () -> generator.generate(row, outDir, template));
  }

  @Test
  void generate_nullLastName_throwsException() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Test");
    row.put("last_name", null);

    String template = "[[first_name]] [[last_name]]";
    Path outDir = Files.createTempDirectory("letter-test");

    assertThrows(TemplateProcessingException.class,
        () -> generator.generate(row, outDir, template));
  }

  @Test
  void generate_bothNamesNull_createsFileWithUnknownNames() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", null);
    row.put("last_name", null);

    String template = "Content";
    Path outDir = Files.createTempDirectory("letter-test");

    generator.generate(row, outDir, template);

    // Both null become "unknown"
    Path expectedFile = outDir.resolve("letter-unknown-unknown.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_singleSpace_becomesUnderscore() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Mary Jane");
    row.put("last_name", "Smith Brown");

    String template = "Hello";
    Path outDir = Files.createTempDirectory("letter-test");

    generator.generate(row, outDir, template);

    Path expectedFile = outDir.resolve("letter-Mary_Jane-Smith_Brown.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_tabAndNewline_replacedWithUnderscore() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "A\tB");
    row.put("last_name", "X\nY");

    String template = "Content";
    Path outDir = Files.createTempDirectory("letter-test");

    generator.generate(row, outDir, template);

    Path expectedFile = outDir.resolve("letter-A_B-X_Y.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_utf8Content_preservesUnicodeCharacters() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "François");
    row.put("last_name", "Müller");

    String template = "Bonjour [[first_name]] [[last_name]]! Résumé: über";
    Path outDir = Files.createTempDirectory("letter-test");

    generator.generate(row, outDir, template);

    Path file = outDir.resolve("letter-François-Müller.txt");
    String content = Files.readString(file, StandardCharsets.UTF_8);
    assertEquals("Bonjour François Müller! Résumé: über", content);
  }

  @Test
  void generate_onlyFirstNameProvided_lastNameNull() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "Victoria");

    String template = "Content";
    Path outDir = Files.createTempDirectory("letter-test");

    generator.generate(row, outDir, template);

    Path expectedFile = outDir.resolve("letter-Victoria-unknown.txt");
    assertTrue(Files.exists(expectedFile));
  }

  @Test
  void generate_fileContentMatchesTemplate() throws IOException {
    Map<String, String> row = new HashMap<>();
    row.put("first_name", "William");
    row.put("last_name", "Shakespeare");

    String template = "To [[first_name]] [[last_name]], the greatest writer.";
    Path outDir = Files.createTempDirectory("letter-test");

    generator.generate(row, outDir, template);

    Path file = outDir.resolve("letter-William-Shakespeare.txt");
    String content = Files.readString(file);

    assertEquals("To William Shakespeare, the greatest writer.", content);
  }
}
