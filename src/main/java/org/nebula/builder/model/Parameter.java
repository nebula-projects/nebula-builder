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

public class Parameter {

  private String type;
  private String name;
  private String typeClass;
  private String boxType;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTypeClass() {
    return typeClass;
  }

  public void setTypeClass(String typeClass) {
    this.typeClass = typeClass;
  }

  public String getBoxType() {
    return boxType;
  }

  public void setBoxType(String boxType) {
    this.boxType = boxType;
  }
}
