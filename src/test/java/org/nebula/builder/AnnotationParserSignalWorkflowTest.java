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

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.nebula.builder.model.Method;

import java.util.Arrays;

import static org.nebula.builder.MethodBuilder.buildTestSignalMethod;

@RunWith(Parameterized.class)
public class AnnotationParserSignalWorkflowTest {

  private final static String POJO_PACKAGE = "org.nebula.builder.test.pojo";

  @Rule
  public CompilationRule rule = new CompilationRule();

  @Parameterized.Parameter
  public String simpleName;

  @Parameterized.Parameter(value = 1)
  public Method[] expectedMethods;

  @Parameterized.Parameters(name = "{index}: parse({0}, {1})={2}")
  public static Iterable<Object[]> data() {
    return Arrays.asList(new Object[][]{

        {"SignalWorkflowWithMultipleParameter", new Method[]{
            buildTestSignalMethod(new String[0][0]),
            buildTestSignalMethod(new String[][]{{"signal", "java.lang.String"}}),
            buildTestSignalMethod(
                new String[][]{{"signal1", "java.lang.String"}, {"signal2", "java.lang.String"}})}},

        {"SignalWorkflowWithPrimitiveParameter", new Method[]{
            buildTestSignalMethod(new String[][]{{"signal", "int", "int", "Integer"}}),
            buildTestSignalMethod(new String[][]{{"signal", "long", "long", "Long"}}),
            buildTestSignalMethod(new String[][]{{"signal", "float", "float", "Float"}}),
            buildTestSignalMethod(new String[][]{{"signal", "double", "double", "Double"}}),
            buildTestSignalMethod(new String[][]{{"signal", "byte", "byte", "Byte"}}),
            buildTestSignalMethod(new String[][]{{"signal", "char", "char", "Character"}}),
            buildTestSignalMethod(new String[][]{{"signal", "short", "short", "Short"}}),
            buildTestSignalMethod(new String[][]{{"signal", "boolean", "boolean", "Boolean"}}),}},

        {"SignalWorkflowWithArrayParameters", new Method[]{
            buildTestSignalMethod(new String[][]{
                {"names", "java.lang.String[]", "[Ljava.lang.String;", "java.lang.String[]"},
                {"ages", "int[]", "[I", "int[]"}})}},

        {"SignalWorkflowWithPojoParameters", new Method[]{
            buildTestSignalMethod(new String[][]{{"person", POJO_PACKAGE + ".Person"},
                                                 {"personCollection",
                                                  "java.util.Collection<" + (POJO_PACKAGE
                                                                             + ".Person") + ">"},
                                                 {"personList",
                                                  "java.util.List<" + (POJO_PACKAGE + ".Person")
                                                  + ">"},
                                                 {"personSet",
                                                  "java.util.Set<" + (POJO_PACKAGE + ".Person")
                                                  + ">"},
                                                 {"personMap",
                                                  "java.util.Map<java.lang.String," + (POJO_PACKAGE
                                                                                       + ".Person")
                                                  + ">"},
                                                 {"personArray", POJO_PACKAGE + ".Person" + "[]",
                                                  "[L" + POJO_PACKAGE + ".Person" + ";",
                                                  POJO_PACKAGE + ".Person" + "[]"}})}}

    });
  }

  @Test
  public void testParse() {
    AnnotationParserExecutionValidator
        validator =
        new AnnotationParserExecutionValidator(rule, simpleName);

    validator.validateWorkflowWithSignalAnnotation(expectedMethods);
  }

}