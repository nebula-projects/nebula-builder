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
import org.nebula.builder.model.Methods;
import org.nebula.framework.annotation.Activity;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;

@SupportedAnnotationTypes("org.nebula.framework.annotation.Activity")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ActivityProcessor extends NebulaFrameworkProcessor {

  private String fqClassName;

  protected Class getSupportedAnnotation() {
    return Activity.class;
  }

  protected VelocityContext getVelocityContext(Element rootElement) {

    VelocityContext vc = new VelocityContext();

    parseActivity(vc, rootElement);

    return vc;
  }

  private void parseActivity(VelocityContext vc, Element rootElement) {

    AnnotationParser parser = new AnnotationParser(
        processingEnv.getTypeUtils());

    parser.parse(rootElement);

    Methods activityMethods = parser.getAllMethods();

    fqClassName = parser.getFqClassName();

    String className = parser.getClassName();
    String packageName = parser.getPackageName();

    vc.put("activityClass", className);
    vc.put("packageName", packageName);
    vc.put("activityMethods", activityMethods.getMethods());

    Activity activity = rootElement.getAnnotation(Activity.class);
    vc.put("activityVersion", activity.version());
    vc.put("activityName", getActivityName(activity, className));
  }

  private String getActivityName(Activity activity, String defaultName) {
    return activity.name() == null
           || activity.name().trim().equals("") ? defaultName
                                                  : activity.name();
  }

  protected String getInterfaceTemplate() {
    return "activity.vm";
  }

  protected String getImplementationTemplate() {
    return "activityImpl.vm";
  }

  protected String getFqInterfaceName() {
    return fqClassName + "Client";
  }

  protected String getFqImplementationName() {
    return fqClassName + "ClientImpl";
  }
}