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

import static org.nebula.builder.MethodBuilder.build;

@RunWith(Parameterized.class)
public class AnnotationParserActivityTest {

  private final static String POJO_PACKAGE = "org.nebula.builder.test.pojo";

  @Rule
  public CompilationRule rule = new CompilationRule();

  @Parameterized.Parameter
  public String simpleName;

  @Parameterized.Parameter(value = 1)
  public Method[] expectedMethods;

  @Parameterized.Parameters(name = "{index}: parse({0})={1}")
  public static Iterable<Object[]> data() {
    return Arrays.asList(new Object[][]{

        {"Activity", new Method[]{
            build("testActivityWithoutReturn", "Void", "java.lang.Void", new String[0][0]),
            build("testActivityWithString", "java.lang.String", "java.lang.String",
                  new String[][]{{"name", "java.lang.String"}}),
            build("testActivityWithPrimitiveInt", "Integer", "int",
                  new String[][]{{"number", "int", "int", "Integer"}}),
            build("testActivityWithPrimitiveLong", "Long", "long",
                  new String[][]{{"number", "long", "long", "Long"}}),
            build("testActivityWithPrimitiveFloat", "Float", "float",
                  new String[][]{{"number", "float", "float", "Float"}}),
            build("testActivityWithPrimitiveDouble", "Double", "double",
                  new String[][]{{"number", "double", "double", "Double"}}),
            build("testActivityWithPrimitiveByte", "Byte", "byte",
                  new String[][]{{"number", "byte", "byte", "Byte"}}),
            build("testActivityWithPrimitiveChar", "Character", "char",
                  new String[][]{{"number", "char", "char", "Character"}}),
            build("testActivityWithPrimitiveShort", "Short", "short",
                  new String[][]{{"number", "short", "short", "Short"}}),
            build("testActivityWithPrimitiveBool", "Boolean", "boolean",
                  new String[][]{{"number", "boolean", "boolean", "Boolean"}}),
            build("testActivityWithPojo", POJO_PACKAGE + ".Person", POJO_PACKAGE + ".Person",
                  new String[][]{{"person", POJO_PACKAGE + ".Person"}}),
            build("testActivityWithList", "java.util.List<" + POJO_PACKAGE + ".Person" + ">",
                  "java.util.List<" + POJO_PACKAGE + ".Person" + ">",
                  new String[][]{{"persons", "java.util.List<" + POJO_PACKAGE + ".Person" + ">"}}),
            build("testActivityWithArray", "long[]", "[J", new String[][]{
                {"names", "java.lang.String[]", "[Ljava.lang.String;", "java.lang.String[]"},
                {"numbers", "long[]", "[J", "long[]"}})
        }}

    });
  }

  @Test
  public void testParse() {
    AnnotationParserExecutionValidator
        validator =
        new AnnotationParserExecutionValidator(rule, simpleName);

    validator.validateActivity(expectedMethods);
  }

}