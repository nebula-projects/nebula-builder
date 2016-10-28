package org.nebula.builder.test.workflow;

import org.nebula.framework.annotation.Start;
import org.nebula.framework.annotation.Workflow;

@Workflow
public interface StartWorkflowWithOneParameter {

  @Start
  public void testStart(String name);

}