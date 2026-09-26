package assignment3;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CsvReaderTest {

  private CsvReader reader;

  @BeforeEach
  void setUp() {
    this.reader = new CsvReader();
  }

  @Test
  void read_simpleCsv_success() throws IOException {
    Path temp = Files.createTempFile("test", ".csv");
    Files.writeString(temp,
        "\"first\",\"last\"\n" +
            "\"Mansa\",\"Patidar\"",
        StandardCharsets.UTF_8);

    List<Map<String, String>> rows = this.reader.read(temp);

    assertEquals(1, rows.size());
    Map<String, String> row = rows.get(0);

    assertEquals("Mansa", row.get("first"));
    assertEquals("Patidar", row.get("last"));
  }

  @Test
  void read_csvWithQuotedComma_success() throws IOException {
    Path temp = Files.createTempFile("test", ".csv");
    Files.writeString(temp,
        "\"name\",\"note\"\n" +
            "\"John\",\"Hello, world\"",
        StandardCharsets.UTF_8);

    List<Map<String, String>> rows = this.reader.read(temp);

    assertEquals(1, rows.size());
    assertEquals("Hello, world", rows.get(0).get("note"));
  }

  @Test
  void read_multipleRows_success() throws IOException {
    Path temp = Files.createTempFile("test", ".csv");
    Files.writeString(temp,
        "\"first\",\"last\"\n" +
            "\"A\",\"One\"\n" +
            "\"B\",\"Two\"",
        StandardCharsets.UTF_8);

    List<Map<String, String>> rows = this.reader.read(temp);

    assertEquals(2, rows.size());
    assertEquals("A", rows.get(0).get("first"));
    assertEquals("Two", rows.get(1).get("last"));
  }

  @Test
  void read_ignoresEmptyLines() throws IOException {
    Path temp = Files.createTempFile("test", ".csv");
    Files.writeString(temp,
        "\"first\",\"last\"\n" +
            "\n" +
            "\"A\",\"One\"\n" +
            "\n",
        StandardCharsets.UTF_8);

    List<Map<String, String>> rows = this.reader.read(temp);

    assertEquals(1, rows.size());
    assertEquals("A", rows.get(0).get("first"));
  }

  @Test
  void read_trimsQuotesCorrectly() throws IOException {
    Path temp = Files.createTempFile("test", ".csv");
    Files.writeString(temp,
        "\"first\",\"last\"\n" +
            "\"  Mansa  \",\"  Patidar  \"",
        StandardCharsets.UTF_8);

    List<Map<String, String>> rows = this.reader.read(temp);

    assertEquals("Mansa", rows.get(0).get("first"));
    assertEquals("Patidar", rows.get(0).get("last"));
  }

  @Test
  void read_emptyFile_returnsEmptyList() throws IOException {
    Path temp = Files.createTempFile("test", ".csv");
    Files.writeString(temp, "", StandardCharsets.UTF_8);

    List<Map<String, String>> rows = this.reader.read(temp);

    assertEquals(0, rows.size());
  }

  @Test
  void read_onlyHeaderLine_returnsEmptyList() throws IOException {
    Path temp = Files.createTempFile("test", ".csv");
    Files.writeString(temp, "\"first\",\"last\"", StandardCharsets.UTF_8);

    List<Map<String, String>> rows = this.reader.read(temp);

    assertEquals(0, rows.size());
  }

  @Test
  void read_moreValuesThanHeaders_onlyUsesValidColumns() throws IOException {
    Path temp = Files.createTempFile("test", ".csv");
    Files.writeString(temp,
        "\"first\",\"last\"\n" +
            "\"A\",\"B\",\"C\",\"D\"",
        StandardCharsets.UTF_8);

    List<Map<String, String>> rows = this.reader.read(temp);

    assertEquals(1, rows.size());
    assertEquals("A", rows.get(0).get("first"));
    assertEquals("B", rows.get(0).get("last"));
    assertEquals(2, rows.get(0).size());
  }

  @Test
  void read_fewerValuesThanHeaders_skipsExtraHeaders() throws IOException {
    Path temp = Files.createTempFile("test", ".csv");
    Files.writeString(temp,
        "\"first\",\"middle\",\"last\"\n" +
            "\"A\",\"B\"",
        StandardCharsets.UTF_8);

    List<Map<String, String>> rows = this.reader.read(temp);

    assertEquals(1, rows.size());
    assertEquals("A", rows.get(0).get("first"));
    assertEquals("B", rows.get(0).get("middle"));
    assertNull(rows.get(0).get("last"));
  }

  @Test
  void read_quotedQuotesInValue_handlesCorrectly() throws IOException {
    Path temp = Files.createTempFile("test", ".csv");
    Files.writeString(temp,
        "\"name\",\"message\"\n" +
            "\"John\",\"He said \\\"hello\\\"\"",
        StandardCharsets.UTF_8);

    List<Map<String, String>> rows = this.reader.read(temp);

    assertEquals(1, rows.size());
  }

  @Test
  void read_complexCsvWithMixedQuotes_success() throws IOException {
    Path temp = Files.createTempFile("test", ".csv");
    Files.writeString(temp,
        "\"id\",\"name\",\"email\",\"note\"\n" +
            "\"1\",\"Alice\",\"alice@example.com\",\"Active, verified\"\n" +
            "\"2\",\"Bob Smith\",\"bob@example.com\",\"New user\"",
        StandardCharsets.UTF_8);

    List<Map<String, String>> rows = this.reader.read(temp);

    assertEquals(2, rows.size());
    assertEquals("Alice", rows.get(0).get("name"));
    assertEquals("Active, verified", rows.get(0).get("note"));
    assertEquals("Bob Smith", rows.get(1).get("name"));
  }

  @Test
  void read_singleColumn_success() throws IOException {
    Path temp = Files.createTempFile("test", ".csv");
    Files.writeString(temp,
        "\"name\"\n" +
            "\"Alice\"\n" +
            "\"Bob\"",
        StandardCharsets.UTF_8);

    List<Map<String, String>> rows = this.reader.read(temp);

    assertEquals(2, rows.size());
    assertEquals("Alice", rows.get(0).get("name"));
    assertEquals("Bob", rows.get(1).get("name"));
  }

  @Test
  void read_rowsWithAllEmptyValues_success() throws IOException {
    Path temp = Files.createTempFile("test", ".csv");
    Files.writeString(temp,
        "\"first\",\"last\"\n" +
            "\"\",\"\"",
        StandardCharsets.UTF_8);

    List<Map<String, String>> rows = this.reader.read(temp);

    assertEquals(1, rows.size());
    assertEquals("", rows.get(0).get("first"));
    assertEquals("", rows.get(0).get("last"));
  }
}
