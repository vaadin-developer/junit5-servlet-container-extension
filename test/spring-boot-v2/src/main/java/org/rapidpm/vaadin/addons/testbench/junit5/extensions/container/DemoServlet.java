/**
 * Copyright © 2017 Sven Ruppert (sven.ruppert@gmail.com)
 * Copyright 2018 Daniel Nordhoff-Vergien (dve@vergien.net)
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

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;


public class DemoServlet extends GenericServlet {

  private int called = 0;

  @Autowired
  private Environment environment;

  @Override
  public void service(ServletRequest request, ServletResponse response)
      throws ServletException, IOException {
    called++;
    response.setContentType("text/plain");
    response.getWriter()
        .append("Hello World on port " + environment.getProperty("local.server.port") + " called " + called + " times");
  }
}
