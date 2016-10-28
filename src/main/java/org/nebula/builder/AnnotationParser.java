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
import org.nebula.builder.model.Methods;
import org.nebula.builder.model.Parameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;


public class AnnotationParser {

  private final static Class[] NONE_ANNOTATIONS = new Class[]{NoneAnnotation.class};
  private final Types typeUtility;
  private Map<Class, Methods> parsedMethods = new HashMap<Class, Methods>();

  private Methods methods = new Methods();

  private String className;

  private String packageName;

  private String fqClassName;

  public AnnotationParser(Types typeUtility) {
    this.typeUtility = typeUtility;
  }

  private static String getPrimitiveArray(TypeMirror type) {
    switch (type.getKind()) {
      case BYTE:
        return "B";
      case SHORT:
        return "S";
      case INT:
        return "I";
      case LONG:
        return "J";
      case FLOAT:
        return "F";
      case DOUBLE:
        return "D";
      case BOOLEAN:
        return "Z";
      case CHAR:
        return "C";
      default:
        return type.toString();
    }
  }

  private static String box(TypeMirror type) {
    switch (type.getKind()) {
      case BYTE:
        return "Byte";
      case SHORT:
        return "Short";
      case INT:
        return "Integer";
      case LONG:
        return "Long";
      case FLOAT:
        return "Float";
      case DOUBLE:
        return "Double";
      case BOOLEAN:
        return "Boolean";
      case CHAR:
        return "Character";
      case VOID:
        return "Void";
      default:
        return type.toString();
    }
  }

  private static String primitive(TypeMirror type) {
    switch (type.getKind()) {
      case BYTE:
        return "byte";
      case SHORT:
        return "short";
      case INT:
        return "int";
      case LONG:
        return "long";
      case FLOAT:
        return "float";
      case DOUBLE:
        return "double";
      case BOOLEAN:
        return "boolean";
      case CHAR:
        return "char";
      default:
        return type.toString();
    }
  }

  /**
   * Affirms if the given type mirrors a primitive.
   */
  private static boolean isPrimitive(TypeMirror mirror) {
    TypeKind kind = mirror.getKind();
    return kind == TypeKind.BOOLEAN || kind == TypeKind.BYTE
           || kind == TypeKind.CHAR || kind == TypeKind.DOUBLE
           || kind == TypeKind.FLOAT || kind == TypeKind.INT
           || kind == TypeKind.LONG || kind == TypeKind.SHORT;
  }

  /**
   * Note: the exception thrown out in method signature is not parsed.
   */
  public void parse(Element rootElement, Class... annotationClazz) {
    Class[] annotationsForParse = annotationClazz;
    if (annotationsForParse == null || annotationsForParse.length == 0) {
      annotationsForParse = NONE_ANNOTATIONS;
    }

    if (rootElement.getKind() == ElementKind.INTERFACE) {

      TypeElement classElement = (TypeElement) rootElement;
      PackageElement packageElement = (PackageElement) classElement
          .getEnclosingElement();

      fqClassName = classElement.getQualifiedName().toString();
      className = classElement.getSimpleName().toString();
      packageName = packageElement.getQualifiedName().toString();

      for (Element e : classElement.getEnclosedElements()) {
        parseAnnotations(e, annotationsForParse);
      }
    }
  }

  private void parseAnnotations(Element e, Class[] annotationsForParse) {

    if (e.getKind() == ElementKind.METHOD) {
      for (Class annotationClass : annotationsForParse) {
        parseAnnotation(e, annotationClass);
      }
    }
  }

  private void parseAnnotation(Element e, Class annotationClass) {

    Method method = new MethodSignatureExtractor((ExecutableElement) e)
        .extract();

    Object annotation = e.getAnnotation(annotationClass);
    if (annotation != null) {
      getMethods(annotationClass).addMethod(method);
    }

    methods.addMethod(method);
  }

  private Methods getMethods(Class annotationClass) {
    Methods methods = parsedMethods.get(annotationClass);
    if (methods == null) {
      methods = new Methods();
      parsedMethods.put(annotationClass, methods);
    }
    return methods;
  }

  private boolean isArray(TypeMirror mirror) {
    if (mirror == null || mirror.getKind() == TypeKind.NULL
        || mirror.getKind() == TypeKind.WILDCARD) {
      return false;
    }

    return mirror.getKind() == TypeKind.ARRAY;
  }

  private String getArrayClass(TypeMirror mirror) {

    if (isArray(mirror)) {
      TypeMirror comp = ((ArrayType) mirror).getComponentType();

      return "[" + getArrayClass(comp);
    }

    if (isPrimitive(mirror)) {
      return getPrimitiveArray(mirror);
    }

    Element elem = typeUtility.asElement(mirror);
    if (elem == null) {
      throw new RuntimeException("The element is null.");
    }

    return "L" + elem.toString() + ";";
  }

  /**
   * Get the element name of the class the given mirror represents. If the mirror is primitive then
   * returns the corresponding boxed class name. If the mirror is parameterized returns only the
   * generic type i.e. if the given declared type is <code>java.util.Set&lt;java.lang.String&gt;</code>
   * this method will return <code>java.util.Set</code>.
   */
  private String getDeclaredTypeName(TypeMirror mirror, boolean boxed) {

    if (mirror == null || mirror.getKind() == TypeKind.NULL
        || mirror.getKind() == TypeKind.WILDCARD) {
      return "java.lang.Object";
    }

    if (mirror.getKind() == TypeKind.VOID) {
      return "java.lang.Void";
    }
//
    if (mirror.getKind() == TypeKind.ARRAY) {
      TypeMirror comp = ((ArrayType) mirror).getComponentType();
      //return getDeclaredTypeName(comp, false);
      return mirror.toString();
    }

    if (isPrimitive(mirror)) {
      return boxed ? box(mirror) : primitive(mirror);
    }

    if (isParameterizedType(mirror)) {
      return mirror.toString().replaceAll(" ", "");
    }
    Element elem = typeUtility.asElement(mirror);
    if (elem == null) {
      throw new RuntimeException("The element is null.");
    }

    return elem.toString().replaceAll(" ", "");
  }

  private boolean isParameterizedType(TypeMirror t) {
    if (t instanceof DeclaredType) {
      DeclaredType d = (DeclaredType) t;
      return !d.getTypeArguments().isEmpty();
    }
    return false;
  }

  public Map<Class, Methods> getParsedMethods() {
    return parsedMethods;
  }

  public Methods getAllMethods() {
    return methods;
  }

  public String getFqClassName() {
    return fqClassName;
  }

  public String getClassName() {
    return className;
  }

  public String getPackageName() {
    return packageName;
  }

  private @interface NoneAnnotation {

  }

  class MethodSignatureExtractor {

    private ExecutableElement exeElement;

    private Method method;

    public MethodSignatureExtractor(ExecutableElement exeElement) {
      this.exeElement = exeElement;
      this.method = new Method();
    }

    public Method extract() {
      extractMethodName();
      extractMethodReturnType();
      extractMethodParameters();

      return method;
    }

    public void extractMethodName() {
      method.setName(exeElement.getSimpleName().toString());
    }

    public void extractMethodReturnType() {

      TypeMirror returnType = exeElement.getReturnType();

      if (returnType.getKind() == TypeKind.DECLARED) {
        method.setReturnType(returnType.toString());
      } else {
        method.setReturnType(box(returnType));
      }

      if (isArray(returnType)) {
        method.setReturnTypeClass(getArrayClass(returnType));
      } else {
        method.setReturnTypeClass(getDeclaredTypeName(returnType, false));
      }
    }

    public void extractMethodParameters() {

      List<Parameter> parameters = new ArrayList<Parameter>();

      for (VariableElement variable : exeElement.getParameters()) {

        Parameter parameter = new Parameter();
        parameter.setName(variable.getSimpleName().toString());

        TypeMirror variableType = variable.asType();

        String type = getDeclaredTypeName(variableType, false);
        parameter.setType(type);
        parameter.setBoxType(getDeclaredTypeName(variableType, true));

        if (isArray(variableType)) {
          parameter.setTypeClass(getArrayClass(variableType));
        } else {
          parameter.setTypeClass(getDeclaredTypeName(variableType, false));
        }
        parameters.add(parameter);
      }

      method.setParameters(parameters);
    }

  }

}
