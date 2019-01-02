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
package junit.org.rapidpm.vaadin.addons.testbench.junit5.extensions.container;

import org.apache.meecrowave.Meecrowave;
import org.apache.meecrowave.junit5.MeecrowaveConfig;
import org.apache.meecrowave.testing.ConfigurationInject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rapidpm.vaadin.addons.testbench.junit5.extensions.container.ServletContainerExtension;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.IOException;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@MeecrowaveConfig
@ExtendWith(ServletContainerExtension.class)
public class DemoTest {

  @Dependent
  public static class UpperCaseService {

    public String upperCase(String txt) {
      return txt.toUpperCase();
    }
  }

  @WebServlet("/*")
  public static class HelloWorldServlet extends HttpServlet {


    @Inject private UpperCaseService service;

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException {
      response.setContentType("text/plain; charset=utf-8");

      String value = request.getParameter("value");

      response.getWriter().println(service.upperCase(value));
    }

  }

  @ConfigurationInject
  private Meecrowave.Builder config;

  @Test
  @Disabled
  public void test001() {
    final Client client = ClientBuilder.newClient();
    try {
      assertEquals("HALLONASE", client.target("http://127.0.0.1:" + config.getHttpPort())
                                      .queryParam("value", "HalloNase")
                                      .request(APPLICATION_JSON_TYPE)
                                      .get(String.class)
                                      .trim());
    } catch (Exception e) {
      fail(e.getMessage());
    } finally {
      client.close();
    }
  }
}
