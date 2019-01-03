package junit.org.rapidpm.vaadin.addons.testbench.junit5.extensions.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rapidpm.vaadin.addons.testbench.junit5.extensions.container.UpperCaseService;

public class UpperCaseServiceTest {

  @Test
  void test001() {
    Assertions.assertEquals("HALLO", new UpperCaseService().upperCase("hallo"));
  }
}
