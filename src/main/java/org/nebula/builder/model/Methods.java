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

package org.nebula.builder.model;

import java.util.LinkedList;
import java.util.List;

public class Methods {

  private List<Method> methods = new LinkedList<Method>();

  public List<Method> getMethods() {
    return methods;
  }

  public void setMethods(List<Method> methods) {
    this.methods = methods;
  }

  public void addMethod(Method method) {
    methods.add(method);
  }
}
