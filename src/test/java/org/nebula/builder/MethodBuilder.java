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

import org.nebula.builder.model.Method;
import org.nebula.builder.model.Parameter;

import java.util.ArrayList;
import java.util.List;

public class MethodBuilder {

  public static Method buildTestStartMethod(String[][] parameters) {

    return build("testStart", "Void", "java.lang.Void", parameters);
  }

  public static Method buildTestSignalMethod(String[][] parameters) {
    return build("testSignal", "Void", "java.lang.Void", parameters);
  }

  public static Method build(String name, String returnType, String returnTypeClass,
                             String[][] parameters) {

    Method method = new Method();
    method.setName(name);
    method.setReturnType(returnType);
    method.setReturnTypeClass(returnTypeClass);
    method.setParameters(build(parameters));

    return method;
  }

  private static List<Parameter> build(String[][] parameterArray) {

    List<Parameter> parameters = new ArrayList<Parameter>();
    for (int i = 0; i < parameterArray.length; i++) {
      Parameter parameter = new Parameter();
      parameter.setName(parameterArray[i][0]);
      parameter.setType(parameterArray[i][1]);

      if (parameterArray[i].length == 2) {
        parameter.setTypeClass(parameterArray[i][1]);
        parameter.setBoxType(parameterArray[i][1]);
      } else if (parameterArray[i].length == 4) {
        parameter.setTypeClass(parameterArray[i][2]);
        parameter.setBoxType(parameterArray[i][3]);
      } else {
        throw new IllegalArgumentException("The parameter array is illegal.");
      }

      parameters.add(parameter);
    }

    return parameters;
  }
}