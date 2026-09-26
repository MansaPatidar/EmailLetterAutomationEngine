package assignment3;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommandLineParserTest {

  private CommandLineParser parser;

  @BeforeEach
  void setUp() {
    this.parser = new CommandLineParser();
  }

  @Test
  void parse_validEmailCommand_returnsConfig() throws Exception {
    String[] args = {
        "--email",
        "--email-template", "email-template.txt",
        "--output-dir", "out",
        "--csv-file", "data.csv"
    };

    ProgramConfig config = this.parser.parse(args);

    assertTrue(config.isGenerateEmail());
    assertFalse(config.isGenerateLetter());
    assertEquals("email-template.txt", config.getEmailTemplatePath().toString());
    assertEquals("out", config.getOutputDir().toString());
    assertEquals("data.csv", config.getCsvFile().toString());
  }

  @Test
  void parse_validLetterCommand_returnsConfig() throws Exception {
    String[] args = {
        "--letter",
        "--letter-template", "letter-template.txt",
        "--output-dir", "out",
        "--csv-file", "data.csv"
    };

    ProgramConfig config = this.parser.parse(args);

    assertFalse(config.isGenerateEmail());
    assertTrue(config.isGenerateLetter());
    assertEquals("letter-template.txt", config.getLetterTemplatePath().toString());
  }

  @Test
  void parse_missingOutputDir_throwsException() {
    String[] args = {
        "--email",
        "--email-template", "email-template.txt",
        "--csv-file", "data.csv"
    };

    assertThrows(InvalidCommandException.class,
        () -> this.parser.parse(args));
  }

  @Test
  void parse_missingCsvFile_throwsException() {
    String[] args = {
        "--email",
        "--email-template", "email-template.txt",
        "--output-dir", "out"
    };

    assertThrows(InvalidCommandException.class,
        () -> this.parser.parse(args));
  }

  @Test
  void parse_missingEmailTemplate_throwsException() {
    String[] args = {
        "--email",
        "--output-dir", "out",
        "--csv-file", "data.csv"
    };

    assertThrows(InvalidCommandException.class,
        () -> this.parser.parse(args));
  }

  @Test
  void parse_bothEmailAndLetter_throwsException() {
    String[] args = {
        "--email",
        "--letter",
        "--email-template", "e.txt",
        "--letter-template", "l.txt",
        "--output-dir", "out",
        "--csv-file", "data.csv"
    };

    assertThrows(InvalidCommandException.class,
        () -> this.parser.parse(args));
  }

  @Test
  void parse_unknownFlag_throwsException() {
    String[] args = {
        "--email",
        "--email-template", "e.txt",
        "--output-dir", "out",
        "--csv-file", "data.csv",
        "--unknown"
    };

    assertThrows(InvalidCommandException.class,
        () -> this.parser.parse(args));
  }

  @Test
  void getUsage_returnsNonEmptyString() {
    String usage = this.parser.getUsage();
    assertNotNull(usage);
    assertFalse(usage.isEmpty());
  }

  @Test
  void parse_neitherEmailNorLetter_throwsException() {
    String[] args = {
        "--output-dir", "out",
        "--csv-file", "data.csv"
    };

    assertThrows(InvalidCommandException.class,
        () -> this.parser.parse(args));
  }

  @Test
  void parse_missingLetterTemplate_throwsException() {
    String[] args = {
        "--letter",
        "--output-dir", "out",
        "--csv-file", "data.csv"
    };

    assertThrows(InvalidCommandException.class,
        () -> this.parser.parse(args));
  }

  @Test
  void parse_optionWithoutValue_throwsException() {
    String[] args = {
        "--email",
        "--email-template",
        "--output-dir", "out",
        "--csv-file", "data.csv"
    };

    assertThrows(InvalidCommandException.class,
        () -> this.parser.parse(args));
  }

  @Test
  void parse_csvFileWithoutValue_throwsException() {
    String[] args = {
        "--email",
        "--email-template", "email.txt",
        "--output-dir", "out",
        "--csv-file"
    };

    assertThrows(InvalidCommandException.class,
        () -> this.parser.parse(args));
  }

  @Test
  void parse_outputDirWithoutValue_throwsException() {
    String[] args = {
        "--email",
        "--email-template", "email.txt",
        "--output-dir"
    };

    assertThrows(InvalidCommandException.class,
        () -> this.parser.parse(args));
  }

  @Test
  void parse_emailTemplateWithoutValue_throwsException() {
    String[] args = {
        "--email",
        "--email-template",
        "--output-dir", "out",
        "--csv-file", "data.csv"
    };

    assertThrows(InvalidCommandException.class,
        () -> this.parser.parse(args));
  }

  @Test
  void parse_letterTemplateWithoutValue_throwsException() {
    String[] args = {
        "--letter",
        "--letter-template",
        "--output-dir", "out",
        "--csv-file", "data.csv"
    };

    assertThrows(InvalidCommandException.class,
        () -> this.parser.parse(args));
  }

  @Test
  void parse_validLetterCommand_letterTemplateNotNull() throws Exception {
    String[] args = {
        "--letter",
        "--letter-template", "letter.txt",
        "--output-dir", "out",
        "--csv-file", "data.csv"
    };

    ProgramConfig config = this.parser.parse(args);

    assertNotNull(config.getLetterTemplatePath());
    assertNull(config.getEmailTemplatePath());
  }

  @Test
  void parse_validEmailCommand_emailTemplateNotNull() throws Exception {
    String[] args = {
        "--email",
        "--email-template", "email.txt",
        "--output-dir", "out",
        "--csv-file", "data.csv"
    };

    ProgramConfig config = this.parser.parse(args);

    assertNotNull(config.getEmailTemplatePath());
    assertNull(config.getLetterTemplatePath());
  }
}
