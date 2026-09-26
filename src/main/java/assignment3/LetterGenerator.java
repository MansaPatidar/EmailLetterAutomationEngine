package assignment3;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Generates letter message files from a template and CSV data.
 */
public class LetterGenerator implements MessageGenerator {

  private static final String FIRST_NAME_KEY = "first_name";
  private static final String LAST_NAME_KEY = "last_name";
  private static final String LETTER_PREFIX = "letter-";
  private static final String FILE_EXTENSION = ".txt";

  private final TemplateEngine templateEngine;

  /**
   * Constructs a LetterGenerator with a template engine.
   *
   * @param templateEngine the engine used to fill templates
   */
  public LetterGenerator(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  /**
   * Generates a letter message file for the given CSV row.
   *
   * @param row the CSV row data to use for template filling
   * @param outputDir the directory where the letter file should be written
   * @param templateText the template text with placeholders
   */
  @Override
  public void generate(Map<String, String> row, Path outputDir, String templateText) {
    String filled = this.templateEngine.fillTemplate(templateText, row);

    String first = sanitize(row.get(FIRST_NAME_KEY));
    String last = sanitize(row.get(LAST_NAME_KEY));

    String fileName = LETTER_PREFIX + first + "-" + last + FILE_EXTENSION;
    Path outputFile = outputDir.resolve(fileName);

    try {
      Files.createDirectories(outputDir);
      Files.write(outputFile, filled.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new TemplateProcessingException("Failed to write letter file: " + outputFile);
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
