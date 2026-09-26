package assignment3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Entry point for the Insurance Company Email Automation program.
 */
public class Main {

  /**
   * Main entry point for the program.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {

    System.out.println("Current working directory: " + System.getProperty("user.dir"));
    System.out.println("Classpath: " + System.getProperty("java.class.path"));

    CommandLineParser parser = new CommandLineParser();
    ProgramConfig config;

    try {
      config = parser.parse(args);
    } catch (InvalidCommandException e) {
      System.err.println(e.getMessage());
      return;
    }

    System.out.println(config);

    Path resolvedCsvFile = resolvePath(config.getCsvFile());
    Path resolvedEmailTemplate = config.getEmailTemplatePath() != null ? resolvePath(config.getEmailTemplatePath()) : null;
    Path resolvedLetterTemplate = config.getLetterTemplatePath() != null ? resolvePath(config.getLetterTemplatePath()) : null;

  
    config = new ProgramConfig(config.isGenerateEmail(), config.isGenerateLetter(),
        resolvedEmailTemplate, resolvedLetterTemplate, config.getOutputDir(), resolvedCsvFile);

    Controller controller = new Controller(
        new CsvReader(),
        new TemplateEngine(),
        new StubNotifier()
    );

    try {
      controller.run(config);
    } catch (IOException | TemplateProcessingException e) {
      System.err.println("Error: " + e);
    }
  }

  /**
   * Resolves a filename to an absolute path by searching for it in project directories.
   * First searches upward from the current directory for src/main/resources, then checks
   * the classpath.
   *
   * @param path the filename or path to resolve
   * @return the resolved path, or the original path if not found
   */
  private static Path resolvePath(Path path) {
    String filename = path.getFileName().toString();

    Path current = Paths.get("").toAbsolutePath();
    
    while (current != null) {
      Path resourcePath = current.resolve("src/main/resources").resolve(filename);
      
      if (Files.exists(resourcePath)) {
        System.out.println("Resource found: " + filename);
        return resourcePath;
      }

      if (Files.exists(current.resolve(filename))) {
        System.out.println("Resource found: " + filename);
        return current.resolve(filename);
      }
      
      current = current.getParent();
    }
    System.out.println("Resource not found: " + filename);
    return path;
  }
}
