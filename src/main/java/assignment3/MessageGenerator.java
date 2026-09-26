package assignment3;

import java.nio.file.Path;
import java.util.Map;

/**
 * Represents a generator that creates a message file for a single CSV row.
 */
public interface MessageGenerator {

  /**
   * Generates a message file for the given row using the provided template.
   *
   * @param row          a map of header to value for a single CSV record
   * @param outputDir    directory where the generated file should be written
   * @param templateText the template text to fill
   */
  void generate(Map<String, String> row, Path outputDir, String templateText);
}
