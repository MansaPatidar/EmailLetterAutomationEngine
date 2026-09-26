package assignment3;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Immutable configuration object representing validated command line options.
 */
public final class ProgramConfig {

  private final boolean generateEmail;
  private final boolean generateLetter;
  private final Path emailTemplatePath;
  private final Path letterTemplatePath;
  private final Path outputDir;
  private final Path csvFile;

  /**
   * Constructs a ProgramConfig with the specified options.
   *
   * @param generateEmail whether to generate email messages
   * @param generateLetter whether to generate letters
   * @param emailTemplatePath path to the email template file (null if not email mode)
   * @param letterTemplatePath path to the letter template file (null if not letter mode)
   * @param outputDir directory where generated files should be written
   * @param csvFile path to the CSV file containing data
   */
  public ProgramConfig(boolean generateEmail,
      boolean generateLetter,
      Path emailTemplatePath,
      Path letterTemplatePath,
      Path outputDir,
      Path csvFile) {

    this.generateEmail = generateEmail;
    this.generateLetter = generateLetter;
    this.emailTemplatePath = emailTemplatePath;
    this.letterTemplatePath = letterTemplatePath;
    this.outputDir = outputDir;
    this.csvFile = csvFile;
  }

  /**
   * Checks if email message generation is enabled.
   *
   * @return true if email generation is enabled
   */
  public boolean isGenerateEmail() {
    return this.generateEmail;
  }

  /**
   * Checks if letter generation is enabled.
   *
   * @return true if letter generation is enabled
   */
  public boolean isGenerateLetter() {
    return this.generateLetter;
  }

  /**
   * Gets the email template file path.
   *
   * @return the email template path, or null if not in email mode
   */
  public Path getEmailTemplatePath() {
    return this.emailTemplatePath;
  }

  /**
   * Gets the letter template file path.
   *
   * @return the letter template path, or null if not in letter mode
   */
  public Path getLetterTemplatePath() {
    return this.letterTemplatePath;
  }

  /**
   * Gets the output directory path.
   *
   * @return the output directory path
   */
  public Path getOutputDir() {
    return this.outputDir;
  }

  /**
   * Gets the CSV file path.
   *
   * @return the CSV file path
   */
  public Path getCsvFile() {
    return this.csvFile;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ProgramConfig)) return false;
    ProgramConfig that = (ProgramConfig) o;
    return this.generateEmail == that.generateEmail
        && this.generateLetter == that.generateLetter
        && Objects.equals(this.emailTemplatePath, that.emailTemplatePath)
        && Objects.equals(this.letterTemplatePath, that.letterTemplatePath)
        && Objects.equals(this.outputDir, that.outputDir)
        && Objects.equals(this.csvFile, that.csvFile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.generateEmail, this.generateLetter,
        this.emailTemplatePath, this.letterTemplatePath,
        this.outputDir, this.csvFile);
  }

  @Override
  public String toString() {
    return "ProgramConfig{" +
        "generateEmail=" + this.generateEmail +
        ", generateLetter=" + this.generateLetter +
        ", emailTemplatePath=" + this.emailTemplatePath +
        ", letterTemplatePath=" + this.letterTemplatePath +
        ", outputDir=" + this.outputDir +
        ", csvFile=" + this.csvFile +
        '}';
  }
}
