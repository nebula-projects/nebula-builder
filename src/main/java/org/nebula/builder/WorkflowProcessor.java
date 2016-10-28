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

import org.apache.velocity.VelocityContext;
import org.nebula.builder.model.Method;
import org.nebula.builder.model.Methods;
import org.nebula.framework.annotation.Signal;
import org.nebula.framework.annotation.Start;
import org.nebula.framework.annotation.Workflow;

import java.util.Map;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;

@SupportedAnnotationTypes("org.nebula.framework.annotation.Workflow")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class WorkflowProcessor extends NebulaFrameworkProcessor {

  private String fqClassName;

  protected Class getSupportedAnnotation() {
    return Workflow.class;
  }

  protected VelocityContext getVelocityContext(Element rootElement) {

    VelocityContext vc = new VelocityContext();

    parseMethod(vc, rootElement);

    return vc;
  }

  private void parseMethod(VelocityContext vc, Element rootElement) {

    AnnotationParser parser = new AnnotationParser(processingEnv.getTypeUtils());
    parser.parse(rootElement, Start.class, Signal.class);

    Map<Class, Methods> parsedMethods = parser.getParsedMethods();

    fqClassName = parser.getFqClassName();
    String className = parser.getClassName();
    String packageName = parser.getPackageName();

    vc.put("workflowClass", className);
    vc.put("packageName", packageName);

    Method startMethod = getStartMethod(parsedMethods);
    vc.put("startMethod", startMethod);

    Methods signalMethods = parsedMethods.get(Signal.class);
    if (signalMethods != null) {
      vc.put("signalMethods", signalMethods.getMethods());
    }
  }

  private Method getStartMethod(Map<Class, Methods> parsedMethods) {
    Methods startMethods = parsedMethods.get(Start.class);
    if (startMethods == null || startMethods.getMethods().size() != 1) {
      throw new RuntimeException(
          "One and only one method can annotated with @Start for one workflow " + fqClassName);
    }

    return startMethods.getMethods().get(0);
  }

  protected String getInterfaceTemplate() {
    return "workflow.vm";
  }

  protected String getImplementationTemplate() {
    return "workflowImpl.vm";
  }

  protected String getFqInterfaceName() {
    return fqClassName + "Client";
  }

  protected String getFqImplementationName() {
    return fqClassName + "ClientImpl";
  }
}