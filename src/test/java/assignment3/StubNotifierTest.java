package assignment3;

import org.junit.jupiter.api.Test;

class StubNotifierTest {

  @Test
  void printEmailStub_printsWithoutException() {
    StubNotifier notifier = new StubNotifier();
    // Should not throw
    notifier.printEmailStub();
  }

  @Test
  void printLetterStub_printsWithoutException() {
    StubNotifier notifier = new StubNotifier();
    // Should not throw
    notifier.printLetterStub();
  }

  @Test
  void canInstantiate() {
    StubNotifier notifier = new StubNotifier();
    assert notifier != null;
  }
}
