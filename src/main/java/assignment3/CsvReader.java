package assignment3;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Reads a CSV file where values are quoted and separated by commas.
 */
public class CsvReader {

  /**
   * Reads a CSV file and returns its contents as a list of maps.
   *
   * @param csvPath the path to the CSV file
   * @return a list of maps, where each map represents a row with headers as keys
   * @throws IOException if the file cannot be read
   */
  public List<Map<String, String>> read(Path csvPath) throws IOException {
    List<Map<String, String>> rows = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {

      String headerLine = reader.readLine();
      if (headerLine == null) {
        return rows;
      }

      List<String> headers = parseCsvLine(headerLine);

      String line;
      while ((line = reader.readLine()) != null) {
        if (line.trim().isEmpty()) continue;

        List<String> values = parseCsvLine(line);
        Map<String, String> row = new HashMap<>();

        for (int i = 0; i < headers.size() && i < values.size(); i++) {
          row.put(headers.get(i), values.get(i));
        }

        rows.add(row);
      }
    }

    return rows;
  }

  /**
   * Parses a CSV line into a list of quoted values.
   *
   * @param line the CSV line to parse
   * @return a list of values from the CSV line
   */
  private List<String> parseCsvLine(String line) {
    List<String> result = new ArrayList<>();
    StringBuilder current = new StringBuilder();
    boolean inQuotes = false;

    for (char c : line.toCharArray()) {
      if (c == '"') {
        inQuotes = !inQuotes;
      } else if (c == ',' && !inQuotes) {
        result.add(trim(current.toString()));
        current.setLength(0);
      } else {
        current.append(c);
      }
    }

    result.add(trim(current.toString()));
    return result;
  }

  private String trim(String value) {
    String v = value.trim();
    if (v.startsWith("\"") && v.endsWith("\"") && v.length() >= 2) {
      return v.substring(1, v.length() - 1);
    }
    return v;
  }
}
