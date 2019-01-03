package org.rapidpm.vaadin.addons.testbench.junit5.extensions.container;

import static org.rapidpm.vaadin.addons.testbench.junit5.extensions.container.ExtensionContextFunctions.containerInfo;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.rapidpm.dependencies.core.logger.HasLogger;

public class ContainerInfoExtension implements BeforeEachCallback, HasLogger {

  private ContainerInfo containerInfo;

  public int getPort() {
    return containerInfo.getPort();
  }

  public String getHost() {
    return containerInfo.getHost();
  }

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    logger().info("ContainerInfoExtension - beforeTestExecution ");
    containerInfo = containerInfo().apply(extensionContext);
    logger().info("ContainerInfoExtension - " + containerInfo);
  }
}
