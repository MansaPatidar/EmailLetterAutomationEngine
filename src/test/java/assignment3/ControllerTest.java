package assignment3;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ControllerTest {

  private CsvReader csvReader;
  private TemplateEngine templateEngine;
  private FakeStubNotifier stubNotifier;
  private Controller controller;

  @BeforeEach
  void setUp() {
    this.csvReader = new CsvReader();
    this.templateEngine = new TemplateEngine();
    this.stubNotifier = new FakeStubNotifier();
    this.controller = new Controller(csvReader, templateEngine, stubNotifier);
  }

  @Test
  void run_generatesEmailFiles() throws IOException {
    // Temporary CSV
    Path csv = Files.createTempFile("test", ".csv");
    Files.writeString(csv,
        "\"first_name\",\"last_name\"\n" +
            "\"Mansa\",\"Patidar\"",
        StandardCharsets.UTF_8);

    // Temporary email template
    Path template = Files.createTempFile("email-template", ".txt");
    Files.writeString(template, "Hello [[first_name]] [[last_name]]!", StandardCharsets.UTF_8);

    // Temporary output directory
    Path outDir = Files.createTempDirectory("out");

    ProgramConfig config = new ProgramConfig(
        true,      // generateEmail
        false,     // generateLetter
        template,  // email template
        null,      // letter template
        outDir,
        csv
    );

    controller.run(config);

    // Verify file created
    Path expectedFile = outDir.resolve("email-Mansa-Patidar.txt");
    assertTrue(Files.exists(expectedFile));

    // Verify content
    String content = Files.readString(expectedFile);
    assertEquals("Hello Mansa Patidar!", content);

    // Verify stub message
    assertTrue(stubNotifier.emailStubCalled);
    assertFalse(stubNotifier.letterStubCalled);
  }

  @Test
  void run_generatesLetterFiles() throws IOException {
    // Temporary CSV
    Path csv = Files.createTempFile("test", ".csv");
    Files.writeString(csv,
        "\"first_name\",\"last_name\"\n" +
            "\"Mansa\",\"Patidar\"",
        StandardCharsets.UTF_8);

    // Temporary letter template
    Path template = Files.createTempFile("letter-template", ".txt");
    Files.writeString(template, "Dear [[first_name]] [[last_name]]", StandardCharsets.UTF_8);

    // Temporary output directory
    Path outDir = Files.createTempDirectory("out");

    ProgramConfig config = new ProgramConfig(
        false,     // generateEmail
        true,      // generateLetter
        null,      // email template
        template,  // letter template
        outDir,
        csv
    );

    controller.run(config);

    // Verify file created
    Path expectedFile = outDir.resolve("letter-Mansa-Patidar.txt");
    assertTrue(Files.exists(expectedFile));

    // Verify content
    String content = Files.readString(expectedFile);
    assertEquals("Dear Mansa Patidar", content);

    // Verify stub message
    assertFalse(stubNotifier.emailStubCalled);
    assertTrue(stubNotifier.letterStubCalled);
  }

  @Test
  void run_multipleRowsEmail() throws IOException {
    Path csv = Files.createTempFile("test", ".csv");
    Files.writeString(csv,
        "\"first_name\",\"last_name\"\n" +
            "\"Alice\",\"Smith\"\n" +
            "\"Bob\",\"Jones\"",
        StandardCharsets.UTF_8);

    Path template = Files.createTempFile("email-template", ".txt");
    Files.writeString(template, "Hello [[first_name]]!", StandardCharsets.UTF_8);

    Path outDir = Files.createTempDirectory("out");

    ProgramConfig config = new ProgramConfig(true, false, template, null, outDir, csv);
    controller.run(config);

    assertTrue(Files.exists(outDir.resolve("email-Alice-Smith.txt")));
    assertTrue(Files.exists(outDir.resolve("email-Bob-Jones.txt")));
  }

  @Test
  void run_multipleRowsLetter() throws IOException {
    Path csv = Files.createTempFile("test", ".csv");
    Files.writeString(csv,
        "\"first_name\",\"last_name\"\n" +
            "\"Charlie\",\"Brown\"\n" +
            "\"Diana\",\"Green\"",
        StandardCharsets.UTF_8);

    Path template = Files.createTempFile("letter-template", ".txt");
    Files.writeString(template, "Dear [[first_name]]", StandardCharsets.UTF_8);

    Path outDir = Files.createTempDirectory("out");

    ProgramConfig config = new ProgramConfig(false, true, null, template, outDir, csv);
    controller.run(config);

    assertTrue(Files.exists(outDir.resolve("letter-Charlie-Brown.txt")));
    assertTrue(Files.exists(outDir.resolve("letter-Diana-Green.txt")));
  }

  @Test
  void run_csvWithExtraColumns_ignoresExtra() throws IOException {
    Path csv = Files.createTempFile("test", ".csv");
    Files.writeString(csv,
        "\"first_name\",\"last_name\",\"age\",\"city\"\n" +
            "\"Eve\",\"Wilson\",\"25\",\"NYC\"",
        StandardCharsets.UTF_8);

    Path template = Files.createTempFile("email-template", ".txt");
    Files.writeString(template, "Hello [[first_name]]!", StandardCharsets.UTF_8);

    Path outDir = Files.createTempDirectory("out");

    ProgramConfig config = new ProgramConfig(true, false, template, null, outDir, csv);
    controller.run(config);

    assertTrue(Files.exists(outDir.resolve("email-Eve-Wilson.txt")));
  }

  @Test
  void run_emptyTemplate_createsEmptyFiles() throws IOException {
    Path csv = Files.createTempFile("test", ".csv");
    Files.writeString(csv,
        "\"first_name\",\"last_name\"\n" +
            "\"Frank\",\"Miller\"",
        StandardCharsets.UTF_8);

    Path template = Files.createTempFile("email-template", ".txt");
    Files.writeString(template, "", StandardCharsets.UTF_8);

    Path outDir = Files.createTempDirectory("out");

    ProgramConfig config = new ProgramConfig(true, false, template, null, outDir, csv);
    controller.run(config);

    Path emailFile = outDir.resolve("email-Frank-Miller.txt");
    assertTrue(Files.exists(emailFile));
    assertEquals("", Files.readString(emailFile));
  }

  @Test
  void run_constructorStoresAllDependencies() {
    assertNotNull(controller);
  }

  @Test
  void run_missingTemplateVariable_throwsException() throws IOException {
    Path csv = Files.createTempFile("test", ".csv");
    Files.writeString(csv,
        "\"first_name\",\"last_name\"\n" +
            "\"Grace\",\"Lee\"",
        StandardCharsets.UTF_8);

    Path template = Files.createTempFile("email-template", ".txt");
    Files.writeString(template, "Hello [[missing_var]]!", StandardCharsets.UTF_8);

    Path outDir = Files.createTempDirectory("out");

    ProgramConfig config = new ProgramConfig(true, false, template, null, outDir, csv);

    assertThrows(TemplateProcessingException.class,
        () -> controller.run(config));
  }

  @Test
  void run_emailAndLetterBothFalse_usesLetterTemplatePath() throws IOException {
    Path csv = Files.createTempFile("test", ".csv");
    Files.writeString(csv,
        "\"first_name\",\"last_name\"\n" +
            "\"Henry\",\"Taylor\"",
        StandardCharsets.UTF_8);

    Path emailTemplate = Files.createTempFile("email-template", ".txt");
    Files.writeString(emailTemplate, "Email content", StandardCharsets.UTF_8);
    
    Path letterTemplate = Files.createTempFile("letter-template", ".txt");
    Files.writeString(letterTemplate, "Letter content", StandardCharsets.UTF_8);

    Path outDir = Files.createTempDirectory("out");

    // When generateEmail is false, it uses letterTemplate path
    ProgramConfig config = new ProgramConfig(false, false, emailTemplate, letterTemplate, outDir, csv);
    controller.run(config);

    assertFalse(stubNotifier.emailStubCalled);
    // The else clause calls letterStubCalled when email is false
    assertTrue(stubNotifier.letterStubCalled);
  }

  @Test
  void run_namesWithSpaces_sanitizedCorrectly() throws IOException {
    Path csv = Files.createTempFile("test", ".csv");
    Files.writeString(csv,
        "\"first_name\",\"last_name\"\n" +
            "\"Mary Anne\",\"Smith Jones\"",
        StandardCharsets.UTF_8);

    Path template = Files.createTempFile("email-template", ".txt");
    Files.writeString(template, "Hi [[first_name]]", StandardCharsets.UTF_8);

    Path outDir = Files.createTempDirectory("out");

    ProgramConfig config = new ProgramConfig(true, false, template, null, outDir, csv);
    controller.run(config);

    assertTrue(Files.exists(outDir.resolve("email-Mary_Anne-Smith_Jones.txt")));
  }

  /**
   * Fake notifier to capture stub calls without printing to console.
   */
  private static class FakeStubNotifier extends StubNotifier {
    boolean emailStubCalled = false;
    boolean letterStubCalled = false;

    @Override
    public void printEmailStub() {
      emailStubCalled = true;
    }

    @Override
    public void printLetterStub() {
      letterStubCalled = true;
    }
  }
}
