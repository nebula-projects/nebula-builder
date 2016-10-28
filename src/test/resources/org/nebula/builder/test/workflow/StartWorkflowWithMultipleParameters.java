package org.nebula.builder.test.workflow;

import org.nebula.framework.annotation.Start;
import org.nebula.framework.annotation.Workflow;

@Workflow
public interface StartWorkflowWithMultipleParameters {

  @Start
  public void testStart(String name, int age);

}