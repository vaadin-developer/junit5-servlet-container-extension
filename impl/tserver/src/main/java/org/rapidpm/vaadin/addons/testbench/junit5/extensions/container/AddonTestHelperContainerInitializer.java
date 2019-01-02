/**
 * Copyright © 2017 Sven Ruppert (sven.ruppert@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.rapidpm.vaadin.addons.testbench.junit5.extensions.container;

import static org.rapidpm.vaadin.addons.junit5.extensions.ExtensionFunctions.store;
import java.lang.reflect.Method;
import java.net.URI;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.rapidpm.dependencies.core.logger.HasLogger;
import org.rapidpm.frp.functions.CheckedFunction;
import org.rapidpm.frp.model.Result;
import org.vaadin.addonhelpers.TServer;
import com.google.auto.service.AutoService;

@AutoService(ContainerInitializer.class)
public class AddonTestHelperContainerInitializer implements ContainerInitializer, HasLogger {

  private static final String TSERVER_SERVER = "tserver-server";

  @Override
  public void beforeAll(Class<?> testClass, ExtensionContext context) throws Exception {
    logger().info("no beforeAll op..");
  }

  @Override
  public void beforeEach(Method testMethod, ExtensionContext context) throws Exception {
    int port = preparePort(context);
    prepareIP(context);
    ((CheckedFunction<Integer, Server>) p -> new TServer().startServer(p)).apply(port)
        .ifPresentOrElse(ok -> {
          Result<Server> server = Result.success(ok);
          store().apply(context).put(TSERVER_SERVER, server);
          logger().info("Started server on port: " + port);
        }, failed -> {
          String message = "failed to start TServer for port " + port + " -> " + failed;
          logger().warning(message);
          throw new RuntimeException(message);
        });
  }

  @Override
  public void afterEach(Method testMethod, ExtensionContext context) throws Exception {
    cleanUpPort(context);
    cleanUpIP(context);

    store().apply(context).get(TSERVER_SERVER, Result.class)
        .flatMap((CheckedFunction<Server, String>) server -> {
          URI serverURI = server.getURI();
          server.stop();
          return serverURI.toASCIIString();
        }).ifPresentOrElse(serverURI -> logger().info("Stopped server on : " + serverURI),
            failed -> logger().warning("no active server available to stop " + failed));
    store().apply(context).remove(TSERVER_SERVER);
  }

  @Override
  public void afterAll(Class<?> testClass, ExtensionContext context) throws Exception {
    logger().info("no afterAll op..");
  }
}
