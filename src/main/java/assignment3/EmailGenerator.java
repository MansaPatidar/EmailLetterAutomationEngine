package assignment3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Generates email message files from a template and CSV data.
 */
public class EmailGenerator implements MessageGenerator {

  private static final String FIRST_NAME_KEY = "first_name";
  private static final String LAST_NAME_KEY = "last_name";
  private static final String EMAIL_PREFIX = "email-";
  private static final String FILE_EXTENSION = ".txt";

  private final TemplateEngine templateEngine;

  /**
   * Constructs an EmailGenerator with a template engine.
   *
   * @param templateEngine the engine used to fill templates
   */
  public EmailGenerator(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  /**
   * Generates an email message file for the given CSV row.
   *
   * @param row the CSV row data to use for template filling
   * @param outputDir the directory where the email file should be written
   * @param templateText the template text with placeholders
   */
  @Override
  public void generate(Map<String, String> row, Path outputDir, String templateText) {
    String filled = this.templateEngine.fillTemplate(templateText, row);

    String first = sanitize(row.get(FIRST_NAME_KEY));
    String last = sanitize(row.get(LAST_NAME_KEY));

    String fileName = EMAIL_PREFIX + first + "-" + last + FILE_EXTENSION;
    Path outputFile = outputDir.resolve(fileName);

    try {
      Files.createDirectories(outputDir);
      Files.write(outputFile, filled.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new TemplateProcessingException("Failed to write email file: " + outputFile);
    }
  }

  /**
   * Sanitizes a string by replacing spaces with underscores.
   *
   * @param value the string to sanitize
   * @return the sanitized string, or "unknown" if the input is null
   */
  private String sanitize(String value) {
    if (value == null) {
      return "unknown";
    }
    return value.replaceAll("\\s+", "_");
  }
}
