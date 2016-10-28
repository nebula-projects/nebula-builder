package org.nebula.builder.test.workflow;

import org.nebula.framework.annotation.Start;
import org.nebula.framework.annotation.Workflow;

@Workflow
public interface StartWorkflowWithoutParameter {

  @Start
  public void testStart();

}