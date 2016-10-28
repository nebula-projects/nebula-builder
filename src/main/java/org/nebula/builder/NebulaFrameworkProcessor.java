/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nebula.builder;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.Writer;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public abstract class NebulaFrameworkProcessor extends AbstractProcessor {

  private VelocityEngine ve;

  @Override
  public boolean process(Set<? extends TypeElement> annotations,
                         RoundEnvironment roundEnv) {

    if (annotations.isEmpty()) {
      return true;
    }

    for (Element rootElement : roundEnv
        .getElementsAnnotatedWith(getSupportedAnnotation())) {

      VelocityEngine ve = createVelocityEngine();

      VelocityContext vc = getVelocityContext(rootElement);

      try {
        Template interfaceVt = ve.getTemplate(getInterfaceTemplate());

        write(vc, interfaceVt, getFqInterfaceName());

        Template implementationVt = ve.getTemplate(getImplementationTemplate());

        write(vc, implementationVt, getFqImplementationName());
      } catch (Exception e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                                                 e.getLocalizedMessage());
        throw new RuntimeException();
      }
    }

    return true;
  }

  private VelocityEngine createVelocityEngine() {
    try {
      Properties props = new Properties();
      URL url = this.getClass().getClassLoader()
          .getResource("velocity.properties");
      props.load(url.openStream());

      ve = new VelocityEngine(props);
      ve.init();
      return ve;
    } catch (Exception e) {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                                               e.getLocalizedMessage());
      throw new RuntimeException(e);
    }
  }

  private void write(VelocityContext vc, Template vt, String fqClassName) {

    Writer writer = null;
    try {
      JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
          fqClassName);

      writer = jfo.openWriter();

      vt.merge(vc, writer);

      writer.close();
    } catch (Exception rnfe) {
      processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                                               rnfe.getLocalizedMessage());
      throw new RuntimeException();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (Exception e) {
          processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                                                   e.getLocalizedMessage());
          throw new RuntimeException(e);
        }
      }
    }
  }

  protected abstract Class getSupportedAnnotation();

  protected abstract String getInterfaceTemplate();

  protected abstract String getImplementationTemplate();

  protected abstract VelocityContext getVelocityContext(Element rootElement);

  protected abstract String getFqInterfaceName();

  protected abstract String getFqImplementationName();

}
