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

import com.google.testing.compile.CompilationRule;

import org.nebula.builder.model.Method;
import org.nebula.builder.model.Parameter;
import org.nebula.framework.annotation.Signal;
import org.nebula.framework.annotation.Start;

import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static org.junit.Assert.assertEquals;

public class AnnotationParserExecutionValidator {

  private final static String WORKFLOW_PACKAGE = "org.nebula.builder.test.workflow";
  private final static String ACTIVITY_PACKAGE = "org.nebula.builder.test.activity";

  private AnnotationParser parser;

  private Elements elements;

  private String simpleName;

  public AnnotationParserExecutionValidator(CompilationRule rule, String simpleName) {
    elements = rule.getElements();
    Types types = rule.getTypes();

    this.simpleName = simpleName;
    parser = new AnnotationParser(types);
  }

  private static void verifyParameters(Parameter parameter, String name, String type) {
    verifyParameters(parameter, name, type, type, type);
  }

  private static void verifyParameters(Parameter parameter, String name, String type,
                                       String typeClass, String boxType) {
    assertEquals(name, parameter.getName());
    assertEquals(type, parameter.getType());
    assertEquals(typeClass, parameter.getTypeClass());
    assertEquals(boxType, parameter.getBoxType());
  }

  private static void verifyMethod(Method expectedMethod, Method method) {

    assertEquals(expectedMethod.getName(), method.getName());
    assertEquals(expectedMethod.getReturnType(), method.getReturnType());
    assertEquals(expectedMethod.getReturnTypeClass(), method.getReturnTypeClass());

    List<Parameter> parameters = method.getParameters();
    List<Parameter> expectedParameters = expectedMethod.getParameters();

    verifyParameters(expectedParameters, parameters);
  }

  private static void verifyParameters(List<Parameter> expectedParameters,
                                       List<Parameter> parameters) {

    assertEquals(expectedParameters.size(), parameters.size());

    for (int i = 0; i < parameters.size(); i++) {

      Parameter parameter = parameters.get(i);
      Parameter expectedParameter = expectedParameters.get(i);

      assertEquals(expectedParameter.getName(), parameter.getName());
      assertEquals(expectedParameter.getType(), parameter.getType());
      assertEquals(expectedParameter.getTypeClass(), parameter.getTypeClass());
      assertEquals(expectedParameter.getBoxType(), parameter.getBoxType());

    }

  }


  public void validateWorkflowWithOnlyStartAnnotation(Method expectedMethod) {

    TypeElement typeElement = elements.getTypeElement(WORKFLOW_PACKAGE + "." + simpleName);
    parser.parse(typeElement, Start.class);

    assertEquals(simpleName, parser.getClassName());
    assertEquals(WORKFLOW_PACKAGE + "." + simpleName, parser.getFqClassName());
    assertEquals(WORKFLOW_PACKAGE, parser.getPackageName());

    assertEquals(1, parser.getParsedMethods().get(Start.class).getMethods().size());

    Method method = parser.getParsedMethods().get(Start.class).getMethods().get(0);

    verifyMethod(expectedMethod, method);
  }

  public void validateWorkflowWithSignalAnnotation(Method[] expectedMethods) {

    TypeElement typeElement = elements.getTypeElement(WORKFLOW_PACKAGE + "." + simpleName);
    parser.parse(typeElement, Start.class, Signal.class);

    List<Method> methods = parser.getParsedMethods().get(Signal.class).getMethods();

    for (int i = 0; i < methods.size(); i++) {

      Method method = methods.get(i);
      Method expectedMethod = expectedMethods[i];

      verifyMethod(expectedMethod, method);
    }
  }

  public void validateActivity(Method[] expectedMethods) {
    TypeElement typeElement = elements.getTypeElement(ACTIVITY_PACKAGE + "." + simpleName);
    parser.parse(typeElement);

    List<Method> methods = parser.getAllMethods().getMethods();

    for (int i = 0; i < methods.size(); i++) {

      Method method = methods.get(i);
      Method expectedMethod = expectedMethods[i];

      verifyMethod(expectedMethod, method);
    }
  }

}