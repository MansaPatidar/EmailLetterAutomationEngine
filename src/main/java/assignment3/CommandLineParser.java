package assignment3;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Parses and validates command line arguments for the program.
 */
public class CommandLineParser {

  private static final String EMAIL = "--email";
  private static final String LETTER = "--letter";
  private static final String EMAIL_TEMPLATE = "--email-template";
  private static final String LETTER_TEMPLATE = "--letter-template";
  private static final String OUTPUT_DIR = "--output-dir";
  private static final String CSV_FILE = "--csv-file";

  /**
   * Parses command line arguments into a ProgramConfig object.
   *
   * @param args the command line arguments
   * @return a validated ProgramConfig object
   * @throws InvalidCommandException if arguments are invalid or missing required options
   */
  public ProgramConfig parse(String[] args) throws InvalidCommandException {

    boolean emailFlag = false;
    boolean letterFlag = false;
    Map<String, String> options = new HashMap<>();

    int i = 0;
    while (i < args.length) {
      String arg = args[i];

      if (EMAIL.equals(arg)) {
        emailFlag = true;
        i++;
      } else if (LETTER.equals(arg)) {
        letterFlag = true;
        i++;
      } else if (EMAIL_TEMPLATE.equals(arg)
          || LETTER_TEMPLATE.equals(arg)
          || OUTPUT_DIR.equals(arg)
          || CSV_FILE.equals(arg)) {

        if (i + 1 >= args.length) {
          throw new InvalidCommandException("Missing value for: " + arg + "\n" + getUsage());
        }

        options.put(arg, args[i + 1]);
        i += 2;

      } else {
        throw new InvalidCommandException("Unknown option: " + arg + "\n" + getUsage());
      }
    }

    if (emailFlag && letterFlag) {
      throw new InvalidCommandException("Cannot use both --email and --letter.\n" + getUsage());
    }

    if (!emailFlag && !letterFlag) {
      throw new InvalidCommandException("Must specify either --email or --letter.\n" + getUsage());
    }

    if (!options.containsKey(OUTPUT_DIR)) {
      throw new InvalidCommandException("Missing required option: --output-dir\n" + getUsage());
    }

    if (!options.containsKey(CSV_FILE)) {
      throw new InvalidCommandException("Missing required option: --csv-file\n" + getUsage());
    }

    Path outputDir = Paths.get(options.get(OUTPUT_DIR));
    Path csvFile = Paths.get(options.get(CSV_FILE));

    Path emailTemplatePath = null;
    Path letterTemplatePath = null;

    if (emailFlag) {
      if (!options.containsKey(EMAIL_TEMPLATE)) {
        throw new InvalidCommandException("Missing --email-template.\n" + getUsage());
      }
      emailTemplatePath = Paths.get(options.get(EMAIL_TEMPLATE));
    }

    if (letterFlag) {
      if (!options.containsKey(LETTER_TEMPLATE)) {
        throw new InvalidCommandException("Missing --letter-template.\n" + getUsage());
      }
      letterTemplatePath = Paths.get(options.get(LETTER_TEMPLATE));
    }

    return new ProgramConfig(emailFlag, letterFlag,
        emailTemplatePath, letterTemplatePath,
        outputDir, csvFile);
  }

  /**
   * Returns the usage information for this program.
   *
   * @return a string describing available command line options
   */
  public String getUsage() {
    return "Usage:\n"
      + "--email                     only generate email messages\n"
      + "--email-template <file>     accept a filename that holds the email template. Required if --email is used\n"
      + "--letter                    only generate letters\n"
      + "--letter-template <file>    accept a filename that holds the letter template. Required if --letter is used\n"
      + "--output-dir <path>         accept the name of a folder, all output is placed in this folder\n"
      + "--csv-file <path>           accept the name of the csv file to process\n"
      + "\nExamples:\n"
      + "--email --email-template email-template.txt --output-dir emails --csv-file customer.csv\n"
      + "--letter --letter-template letter-template.txt --output-dir letters --csv-file customer.csv\n"
      + "\nError: --email provided but no --email-template was given.\n"
      + "--email --letter-template letter-template.txt --output-dir letters\n";
  }
}
