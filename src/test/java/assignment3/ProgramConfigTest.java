package assignment3;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class ProgramConfigTest {

  @Test
  void constructor_storesAllFields() {
    Path emailTemplate = Paths.get("email.txt");
    Path letterTemplate = Paths.get("letter.txt");
    Path outputDir = Paths.get("out");
    Path csvFile = Paths.get("data.csv");

    ProgramConfig config = new ProgramConfig(true, false, emailTemplate, letterTemplate, outputDir, csvFile);

    assertTrue(config.isGenerateEmail());
    assertFalse(config.isGenerateLetter());
    assertEquals(emailTemplate, config.getEmailTemplatePath());
    assertEquals(letterTemplate, config.getLetterTemplatePath());
    assertEquals(outputDir, config.getOutputDir());
    assertEquals(csvFile, config.getCsvFile());
  }

  @Test
  void toString_includesAllFields() {
    Path emailTemplate = Paths.get("email.txt");
    Path outputDir = Paths.get("out");
    Path csvFile = Paths.get("data.csv");

    ProgramConfig config = new ProgramConfig(true, false, emailTemplate, null, outputDir, csvFile);
    String str = config.toString();

    assertNotNull(str);
    assertTrue(str.contains("generateEmail=true"));
    assertTrue(str.contains("generateLetter=false"));
    assertTrue(str.contains("email.txt"));
    assertTrue(str.contains("out"));
    assertTrue(str.contains("data.csv"));
  }

  @Test
  void equals_sameValues_returnsTrue() {
    Path emailTemplate = Paths.get("email.txt");
    Path outputDir = Paths.get("out");
    Path csvFile = Paths.get("data.csv");

    ProgramConfig config1 = new ProgramConfig(true, false, emailTemplate, null, outputDir, csvFile);
    ProgramConfig config2 = new ProgramConfig(true, false, emailTemplate, null, outputDir, csvFile);

    assertEquals(config1, config2);
  }

  @Test
  void equals_differentValues_returnsFalse() {
    Path emailTemplate = Paths.get("email.txt");
    Path outputDir = Paths.get("out");
    Path csvFile = Paths.get("data.csv");

    ProgramConfig config1 = new ProgramConfig(true, false, emailTemplate, null, outputDir, csvFile);
    ProgramConfig config2 = new ProgramConfig(false, true, null, emailTemplate, outputDir, csvFile);

    assertNotEquals(config1, config2);
  }

  @Test
  void equals_selfComparison_returnsTrue() {
    ProgramConfig config = new ProgramConfig(true, false, Paths.get("email.txt"), null, Paths.get("out"), Paths.get("data.csv"));

    assertEquals(config, config);
  }

  @Test
  void equals_differentType_returnsFalse() {
    ProgramConfig config = new ProgramConfig(true, false, Paths.get("email.txt"), null, Paths.get("out"), Paths.get("data.csv"));

    assertNotEquals(config, "not a config");
    assertNotEquals(config, null);
  }

  @Test
  void hashCode_sameValues_sameHash() {
    Path emailTemplate = Paths.get("email.txt");
    Path outputDir = Paths.get("out");
    Path csvFile = Paths.get("data.csv");

    ProgramConfig config1 = new ProgramConfig(true, false, emailTemplate, null, outputDir, csvFile);
    ProgramConfig config2 = new ProgramConfig(true, false, emailTemplate, null, outputDir, csvFile);

    assertEquals(config1.hashCode(), config2.hashCode());
  }

  @Test
  void hashCode_differentValues_likelyDifferentHash() {
    ProgramConfig config1 = new ProgramConfig(true, false, Paths.get("email.txt"), null, Paths.get("out"), Paths.get("data.csv"));
    ProgramConfig config2 = new ProgramConfig(false, true, null, Paths.get("letter.txt"), Paths.get("out"), Paths.get("data.csv"));

    assertNotEquals(config1.hashCode(), config2.hashCode());
  }

  @Test
  void isGenerateEmail_emailMode_returnsTrue() {
    ProgramConfig config = new ProgramConfig(true, false, Paths.get("email.txt"), null, Paths.get("out"), Paths.get("data.csv"));

    assertTrue(config.isGenerateEmail());
  }

  @Test
  void isGenerateLetter_letterMode_returnsTrue() {
    ProgramConfig config = new ProgramConfig(false, true, null, Paths.get("letter.txt"), Paths.get("out"), Paths.get("data.csv"));

    assertTrue(config.isGenerateLetter());
  }

  @Test
  void equals_nullPaths_equal() {
    ProgramConfig config1 = new ProgramConfig(true, false, null, null, Paths.get("out"), Paths.get("data.csv"));
    ProgramConfig config2 = new ProgramConfig(true, false, null, null, Paths.get("out"), Paths.get("data.csv"));

    assertEquals(config1, config2);
  }

  @Test
  void equals_differentGenerateEmail_notEqual() {
    Path outputDir = Paths.get("out");
    Path csvFile = Paths.get("data.csv");

    ProgramConfig config1 = new ProgramConfig(true, false, Paths.get("e.txt"), null, outputDir, csvFile);
    ProgramConfig config2 = new ProgramConfig(false, false, Paths.get("e.txt"), null, outputDir, csvFile);

    assertNotEquals(config1, config2);
  }

  @Test
  void equals_differentGenerateLetter_notEqual() {
    Path outputDir = Paths.get("out");
    Path csvFile = Paths.get("data.csv");

    ProgramConfig config1 = new ProgramConfig(true, false, Paths.get("e.txt"), null, outputDir, csvFile);
    ProgramConfig config2 = new ProgramConfig(true, true, Paths.get("e.txt"), Paths.get("l.txt"), outputDir, csvFile);

    assertNotEquals(config1, config2);
  }

  @Test
  void equals_differentEmailTemplate_notEqual() {
    Path outputDir = Paths.get("out");
    Path csvFile = Paths.get("data.csv");

    ProgramConfig config1 = new ProgramConfig(true, false, Paths.get("email1.txt"), null, outputDir, csvFile);
    ProgramConfig config2 = new ProgramConfig(true, false, Paths.get("email2.txt"), null, outputDir, csvFile);

    assertNotEquals(config1, config2);
  }

  @Test
  void equals_differentLetterTemplate_notEqual() {
    Path outputDir = Paths.get("out");
    Path csvFile = Paths.get("data.csv");

    ProgramConfig config1 = new ProgramConfig(false, true, null, Paths.get("letter1.txt"), outputDir, csvFile);
    ProgramConfig config2 = new ProgramConfig(false, true, null, Paths.get("letter2.txt"), outputDir, csvFile);

    assertNotEquals(config1, config2);
  }

  @Test
  void equals_differentOutputDir_notEqual() {
    Path emailTemplate = Paths.get("e.txt");
    Path csvFile = Paths.get("data.csv");

    ProgramConfig config1 = new ProgramConfig(true, false, emailTemplate, null, Paths.get("out1"), csvFile);
    ProgramConfig config2 = new ProgramConfig(true, false, emailTemplate, null, Paths.get("out2"), csvFile);

    assertNotEquals(config1, config2);
  }

  @Test
  void equals_differentCsvFile_notEqual() {
    Path emailTemplate = Paths.get("e.txt");
    Path outputDir = Paths.get("out");

    ProgramConfig config1 = new ProgramConfig(true, false, emailTemplate, null, outputDir, Paths.get("data1.csv"));
    ProgramConfig config2 = new ProgramConfig(true, false, emailTemplate, null, outputDir, Paths.get("data2.csv"));

    assertNotEquals(config1, config2);
  }

  @Test
  void equals_oneWithNullTemplatePath_notEqual() {
    Path outputDir = Paths.get("out");
    Path csvFile = Paths.get("data.csv");
    Path emailTemplate = Paths.get("e.txt");

    ProgramConfig config1 = new ProgramConfig(true, false, emailTemplate, null, outputDir, csvFile);
    ProgramConfig config2 = new ProgramConfig(true, false, null, null, outputDir, csvFile);

    assertNotEquals(config1, config2);
  }

  @Test
  void hashCode_sameInstanceMultipleCalls_consistent() {
    ProgramConfig config = new ProgramConfig(true, false, Paths.get("e.txt"), null, Paths.get("out"), Paths.get("data.csv"));

    int hash1 = config.hashCode();
    int hash2 = config.hashCode();

    assertEquals(hash1, hash2);
  }

  @Test
  void toString_containsBooleanFlags() {
    ProgramConfig config = new ProgramConfig(true, false, Paths.get("e.txt"), null, Paths.get("out"), Paths.get("data.csv"));

    String str = config.toString();
    assertTrue(str.contains("true"));
    assertTrue(str.contains("false"));
  }

  @Test
  void toString_containsProgramConfigClass() {
    ProgramConfig config = new ProgramConfig(true, false, Paths.get("e.txt"), null, Paths.get("out"), Paths.get("data.csv"));

    String str = config.toString();
    assertTrue(str.contains("ProgramConfig"));
  }

  @Test
  void getters_allPathsReturned() {
    Path emailTemplate = Paths.get("email.txt");
    Path letterTemplate = Paths.get("letter.txt");
    Path outputDir = Paths.get("output");
    Path csvFile = Paths.get("data.csv");

    ProgramConfig config = new ProgramConfig(true, true, emailTemplate, letterTemplate, outputDir, csvFile);

    assertEquals(emailTemplate, config.getEmailTemplatePath());
    assertEquals(letterTemplate, config.getLetterTemplatePath());
    assertEquals(outputDir, config.getOutputDir());
    assertEquals(csvFile, config.getCsvFile());
  }

  @Test
  void constructor_bothGenerateTrue_bothStoredCorrectly() {
    ProgramConfig config = new ProgramConfig(true, true, Paths.get("e.txt"), Paths.get("l.txt"), Paths.get("out"), Paths.get("data.csv"));

    assertTrue(config.isGenerateEmail());
    assertTrue(config.isGenerateLetter());
  }

  @Test
  void constructor_bothGenerateFalse_bothStoredCorrectly() {
    ProgramConfig config = new ProgramConfig(false, false, Paths.get("e.txt"), Paths.get("l.txt"), Paths.get("out"), Paths.get("data.csv"));

    assertFalse(config.isGenerateEmail());
    assertFalse(config.isGenerateLetter());
  }

  @Test
  void equals_notProgramConfig_returnsFalse() {
    ProgramConfig config = new ProgramConfig(true, false, Paths.get("e.txt"), null, Paths.get("out"), Paths.get("data.csv"));

    assertNotEquals(config, "not a config");
    assertNotEquals(config, 42);
    assertNotEquals(config, new Object());
  }

  @Test
  void equals_null_returnsFalse() {
    ProgramConfig config = new ProgramConfig(true, false, Paths.get("e.txt"), null, Paths.get("out"), Paths.get("data.csv"));

    assertNotEquals(config, null);
  }
}
