package assignment3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * Manages reading CSV, filling templates, generating messages, and printing stub notifications.
 */
public class Controller {

  private final CsvReader csvReader;
  private final TemplateEngine templateEngine;
  private final StubNotifier stubNotifier;

  /**
   * Constructs a Controller with the necessary dependencies.
   *
   * @param csvReader the CSV file reader
   * @param templateEngine the template engine for filling templates
   * @param stubNotifier the notifier for printing stubs
   */
  public Controller(CsvReader csvReader,
      TemplateEngine templateEngine,
      StubNotifier stubNotifier) {

    this.csvReader = csvReader;
    this.templateEngine = templateEngine;
    this.stubNotifier = stubNotifier;
  }

  /**
   * Executes the main workflow: reads CSV, fills templates, generates messages.
   *
   * @param config the program configuration
   * @throws IOException if file reading or writing fails
   */
  public void run(ProgramConfig config) throws IOException {


    List<Map<String, String>> rows = this.csvReader.read(config.getCsvFile());

    String template = readTemplate(config.isGenerateEmail()
        ? config.getEmailTemplatePath()
        : config.getLetterTemplatePath());

    MessageGenerator generator = config.isGenerateEmail()
        ? new EmailGenerator(this.templateEngine)
        : new LetterGenerator(this.templateEngine);

    for (Map<String, String> row : rows) {
      generator.generate(row, config.getOutputDir(), template);
    }

    if (config.isGenerateEmail()) {
      this.stubNotifier.printEmailStub();
    } else {
      this.stubNotifier.printLetterStub();
    }
  }

  /**
   * Reads a template file and returns its content as a string.
   *
   * @param path the path to the template file
   * @return the template content
   * @throws IOException if the file cannot be read
   */
  private String readTemplate(java.nio.file.Path path) throws IOException {
    byte[] bytes = Files.readAllBytes(path);
    return new String(bytes, StandardCharsets.UTF_8);
  }
}
