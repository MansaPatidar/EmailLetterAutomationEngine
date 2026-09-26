package assignment3;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Processes template strings by replacing placeholders with values from a data map.
 * Placeholders are in the format [[key]].
 */
public class TemplateEngine {

  private static final Pattern PLACEHOLDER_PATTERN =
      Pattern.compile("\\[\\[([^\\]]+)]]");

  /**
   * Fills a template string by replacing all placeholders with corresponding values.
   *
   * @param template the template string containing placeholders in the format [[key]]
   * @param row      a map containing key-value pairs to replace placeholders
   * @return the filled template with all placeholders replaced by their values
   * @throws TemplateProcessingException if a placeholder key is not found in the row map
   */
  public String fillTemplate(String template, Map<String, String> row) {
    Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
    StringBuilder result = new StringBuilder();

    while (matcher.find()) {
      String key = matcher.group(1).trim();
      String value = row.get(key);

      if (value == null) {
        throw new TemplateProcessingException("Missing value for: " + key);
      }

      matcher.appendReplacement(result, Matcher.quoteReplacement(value));
    }

    matcher.appendTail(result);
    return result.toString();
  }
}
