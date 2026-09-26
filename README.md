# EmailLetterAutomationEngine

**An Academic Assignment Project for CS5010: Object-Oriented Design**

![Java](https://img.shields.io/badge/Java-17-orange)
![Gradle](https://img.shields.io/badge/Gradle-Build-blue)
![JUnit](https://img.shields.io/badge/JUnit-5-green)
![Code Coverage](https://img.shields.io/badge/Coverage-70%25+-brightgreen)

## 📋 Overview

EmailLetterAutomationEngine is a Java-based application that automates the generation of personalized emails and letters for insurance company members. The system reads customer data from CSV files and uses template files with dynamic placeholders to generate customized communications.

**🎓 Academic Context:**  
This is an assignment from **Northeastern University's CS5010: Object-Oriented Design** course. It demonstrates key OOP principles including design patterns, separation of concerns, proper exception handling, and comprehensive testing practices.

## ✨ Features

- **Personalized Document Generation**: Automatically generates emails and letters by replacing template placeholders with customer data
- **CSV Data Processing**: Robust CSV parsing supporting quoted values with embedded commas
- **Template Engine**: Regex-based template placeholder replacement with `[[placeholder]]` syntax
- **Command-Line Interface**: Flexible argument parsing with comprehensive validation
- **Stub Notification System**: Simulates email/letter sending with logging
- **Comprehensive Testing**: JUnit 5 test suite with 70%+ code coverage
- **Code Quality**: PMD static analysis and comprehensive Javadoc documentation

## 🏗️ Architecture

The project follows a **Controller-Service** architectural pattern with clear separation of concerns:

```
Main
  └── CommandLineParser
  │     └── Validates command-line arguments
  │
  └── Controller
        ├── CsvReader (reads member data)
        ├── TemplateEngine (processes templates)
        ├── MessageGenerator (interface)
        │    ├── EmailGenerator
        │    └── LetterGenerator
        └── StubNotifier (logs operations)
```

### Key Classes

| Class | Responsibility |
|-------|-----------------|
| `Main` | Program entry point, path resolution |
| `CommandLineParser` | Parses and validates CLI arguments |
| `ProgramConfig` | Stores validated configuration |
| `Controller` | Orchestrates the workflow |
| `CsvReader` | Reads and parses CSV data |
| `TemplateEngine` | Performs template placeholder replacement |
| `MessageGenerator` | Abstract base for email/letter generation |
| `StubNotifier` | Simulates message sending |

## 📦 Prerequisites

- **Java Development Kit (JDK)**: Version 17 or higher
- **Gradle**: 7.0+ (included in project)
- **Git**: For cloning the repository

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/EmailLetterAutomationEngine.git
cd EmailLetterAutomationEngine
```

### 2. Build the Project

Run a complete build with tests and code quality checks:

```bash
./gradlew doAll
```

This command will:
- Compile the Java source code
- Run all JUnit tests
- Generate test coverage reports (JaCoCo)
- Run static analysis (PMD)
- Generate Javadoc documentation

### 3. Run the Application

Use the Gradle application plugin:

```bash
./gradlew run --args="--email --email-template src/main/resources/email-template.txt --csv-file src/main/resources/insurance-company-members.csv --output-dir out"
```

Or compile and run directly:

```bash
./gradlew build
java -cp build/libs/assignment3.jar assignment3.Main --email --email-template src/main/resources/email-template.txt --csv-file src/main/resources/insurance-company-members.csv --output-dir out
```

## 📝 Command-Line Parameters

| Parameter | Type | Description | Required |
|-----------|------|-------------|----------|
| `--email` | Flag | Generate emails | Yes (or `--letter`) |
| `--letter` | Flag | Generate letters | Yes (or `--email`) |
| `--csv-file` | Path | Path to CSV file with member data | Yes |
| `--email-template` | Path | Path to email template file | If `--email` specified |
| `--letter-template` | Path | Path to letter template file | If `--letter` specified |
| `--output-dir` | Path | Output directory for generated files | Yes |

### Usage Examples

**Generate emails only:**
```bash
./gradlew run --args="--email --email-template src/main/resources/email-template.txt --csv-file src/main/resources/insurance-company-members.csv --output-dir out"
```

**Generate letters only:**
```bash
./gradlew run --args="--letter --letter-template src/main/resources/letter-template.txt --csv-file src/main/resources/insurance-company-members.csv --output-dir out"
```

**Generate both emails and letters:**
```bash
./gradlew run --args="--email --letter --email-template src/main/resources/email-template.txt --letter-template src/main/resources/letter-template.txt --csv-file src/main/resources/insurance-company-members.csv --output-dir out"
```

## 📊 Template Format

Templates use `[[placeholder]]` syntax for dynamic content. Example email template:

```
Subject: Your Insurance Policy - [[policy_number]]

Dear [[first_name]] [[last_name]],

Thank you for choosing our insurance services. Your policy details are:
- Policy Number: [[policy_number]]
- Coverage: [[coverage_type]]
- Premium: $[[premium]]

Best regards,
Insurance Company
```

## 📂 Project Structure

```
.
├── src/
│   ├── main/
│   │   ├── java/assignment3/
│   │   │   ├── Main.java
│   │   │   ├── CommandLineParser.java
│   │   │   ├── Controller.java
│   │   │   ├── CsvReader.java
│   │   │   ├── TemplateEngine.java
│   │   │   ├── MessageGenerator.java
│   │   │   ├── EmailGenerator.java
│   │   │   ├── LetterGenerator.java
│   │   │   ├── StubNotifier.java
│   │   │   ├── ProgramConfig.java
│   │   │   └── InvalidCommandException.java
│   │   └── resources/
│   │       ├── email-template.txt
│   │       ├── letter-template.txt
│   │       └── insurance-company-members.csv
│   └── test/
│       └── java/assignment3/
│           ├── MainTest.java
│           ├── CommandLineParserTest.java
│           ├── ControllerTest.java
│           ├── CsvReaderTest.java
│           ├── TemplateEngineTest.java
│           ├── EmailGeneratorTest.java
│           ├── LetterGeneratorTest.java
│           ├── ProgramConfigTest.java
│           ├── StubNotifierTest.java
│           ├── InvalidCommandExceptionTest.java
│           └── TemplateProcessingExceptionTest.java
├── build.gradle
├── settings.gradle
├── gradlew
└── README.md
```

## 🧪 Testing

Run the test suite:

```bash
./gradlew test
```

Generate test coverage report (JaCoCo):

```bash
./gradlew jacocoTestReport
```

The coverage report will be available at `build/jacocoHtml/test/html/index.html`

**Minimum Coverage**: 70% of code is covered by tests (enforced by build)

## 📚 Documentation

Generate Javadoc documentation:

```bash
./gradlew javadoc
```

Javadoc will be generated in `build/docs/javadoc/` directory.

## 🔍 Code Quality

Static analysis using PMD:

```bash
./gradlew pmdMain
```

## 🛠️ Troubleshooting

| Issue | Solution |
|-------|----------|
| File not found errors | Ensure CSV and template files exist at specified paths |
| Missing required arguments | Check that all required arguments are provided; run with `--help` |
| Illegal argument exception | Verify output directory is writable; it will be created if missing |
| Tests fail | Ensure JDK 17+ is installed and JAVA_HOME is set correctly |

## 📋 Assignment Requirements

This project fulfills the following CS5010 assignment requirements:

✅ Read CSV files with proper quote handling  
✅ Process templates with dynamic placeholder replacement  
✅ Command-line argument parsing with validation  
✅ File I/O operations and error handling  
✅ Comprehensive Javadoc comments  
✅ JUnit test suite with 70%+ code coverage  
✅ Follow Java naming conventions  
✅ Use Gradle build system  
✅ Static code analysis with PMD  
✅ UML class diagrams in documentation  

## 🤝 Design Patterns Used

- **Dependency Injection**: Controller accepts dependencies via constructor
- **Strategy Pattern**: MessageGenerator interface with EmailGenerator/LetterGenerator implementations
- **Template Method**: Base processing logic in Controller
- **Factory Pattern**: Implicit in Controller's generator creation

## 📄 License

This is an academic project created for educational purposes as part of Northeastern University's CS5010 course.

## 👤 Author

Created as an assignment for CS5010: Object-Oriented Design

---

**Note**: This project is an educational implementation. The stub notification system simulates email/letter sending for demonstration purposes and does not actually send communications.

For questions about the implementation or assignment, please review the inline Javadoc documentation in the source files.


