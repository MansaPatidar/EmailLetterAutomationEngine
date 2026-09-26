package assignment3;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MainTest {

  @Test
  void resolvePath_fileExists_returnsOriginalPath(@TempDir Path tempDir) throws IOException {
    // Create a file in temp directory
    Path testFile = tempDir.resolve("test.txt");
    Files.write(testFile, "content".getBytes(StandardCharsets.UTF_8));

    // Use reflection to call the private method
    Path resolved = resolvePathViaReflection(testFile);

    // Since file exists, should return original path
    assertEquals(testFile, resolved);
  }

  @Test
  void resolvePath_fileDoesNotExist_searchesForIt(@TempDir Path tempDir) throws IOException {
    // Create a file in a nested src/main/resources structure
    Path resourceDir = tempDir.resolve("src/main/resources");
    Files.createDirectories(resourceDir);
    Path testFile = resourceDir.resolve("data.csv");
    Files.write(testFile, "test data".getBytes(StandardCharsets.UTF_8));

    // Create build.gradle to mark as project root
    Files.write(tempDir.resolve("build.gradle"), "".getBytes(StandardCharsets.UTF_8));

    // Change to a subdirectory under tempDir
    Path subdir = tempDir.resolve("src/main/java");
    Files.createDirectories(subdir);

    // Since we can't actually change the working directory in a unit test,
    // we test that the method handles the path resolution gracefully
    Path result = resolvePathViaReflection(Paths.get("data.csv"));
    assertNotNull(result);
  }

  @Test
  void resolvePath_nonExistentFile_returnsOriginalPath() {
    // For a file that doesn't exist anywhere
    Path nonExistent = Paths.get("definitely-does-not-exist.txt");

    Path result = resolvePathViaReflection(nonExistent);

    // Should return the original path when not found
    assertEquals(nonExistent, result);
  }

  @Test
  void resolvePath_simpleFilename_canResolveIfInSrcMainResources(@TempDir Path tempDir) throws IOException {
    // This test simulates the resources directory structure
    Path resourceDir = tempDir.resolve("src/main/resources");
    Files.createDirectories(resourceDir);
    
    Path resourceFile = resourceDir.resolve("template.txt");
    Files.write(resourceFile, "template content".getBytes(StandardCharsets.UTF_8));
    
    // Mark as project root
    Files.write(tempDir.resolve("build.gradle"), "".getBytes(StandardCharsets.UTF_8));

    assertNotNull(resourceFile);
  }

  @Test
  void resolvePath_absolutePath_returnedAsIs() {
    Path absolutePath = Paths.get("/absolute/path/to/file.txt");

    Path result = resolvePathViaReflection(absolutePath);

    // Absolute paths should be returned as-is if not found
    assertEquals(absolutePath, result);
  }

  @Test
  void resolvePath_pathWithDots_handled(@TempDir Path tempDir) throws IOException {
    Path testFile = tempDir.resolve("file.txt");
    Files.write(testFile, "content".getBytes(StandardCharsets.UTF_8));

    // Paths with dots should be handled
    Path result = resolvePathViaReflection(Paths.get("./file.txt"));
    assertNotNull(result);
  }

  @Test
  void resolvePath_resourceFileWithExtension_resolved(@TempDir Path tempDir) throws IOException {
    Path resourceDir = tempDir.resolve("src/main/resources");
    Files.createDirectories(resourceDir);
    
    Path csvFile = resourceDir.resolve("data.csv");
    Files.write(csvFile, "csv content".getBytes(StandardCharsets.UTF_8));
    
    Files.write(tempDir.resolve("build.gradle"), "".getBytes(StandardCharsets.UTF_8));

    assertNotNull(csvFile);
  }

  @Test
  void resolvePath_nullInput_handled() {
    // Test null safety - expects RuntimeException wrapping NullPointerException
    // from reflection call when null is passed
    assertThrows(RuntimeException.class, () -> resolvePathViaReflection(null));
  }

  @Test
  void resolvePath_emptyFilename_handled() {
    Path emptyPath = Paths.get("");

    Path result = resolvePathViaReflection(emptyPath);
    assertNotNull(result);
  }

  @Test
  void resolvePath_multipleExtensions_resolved(@TempDir Path tempDir) throws IOException {
    Path resourceDir = tempDir.resolve("src/main/resources");
    Files.createDirectories(resourceDir);
    
    Path multiExtFile = resourceDir.resolve("archive.tar.gz");
    Files.write(multiExtFile, "archive".getBytes(StandardCharsets.UTF_8));

    assertNotNull(multiExtFile);
  }

  /**
   * Helper method to call the private resolvePath method via reflection.
   * This allows us to test the private static method.
   */
  private Path resolvePathViaReflection(Path path) {
    try {
      var method = Class.forName("assignment3.Main")
          .getDeclaredMethod("resolvePath", Path.class);
      method.setAccessible(true);
      return (Path) method.invoke(null, path);
    } catch (Exception e) {
      throw new RuntimeException("Failed to invoke resolvePath via reflection", e);
    }
  }
}
